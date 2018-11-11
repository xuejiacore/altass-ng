package org.chim.altass.core.manager.monitor;


import org.chim.altass.core.constant.SystemEnv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;

public class SystemInfoTools {
    private final static boolean isNotWindows = !System.getProperties().getProperty("os.name").toLowerCase().contains("windows");
    private final static BigDecimal DIVISOR = BigDecimal.valueOf(1024);
    private static final String LINUX_CPU_LOAD_AVERAGE_TAG = "load average";
    private static final String LINUX_CPU_PERCENT_TAG = "Cpu(s):";
    private static final String LINUX_MEM_DETAIL_TAG = "Mem :";

    /**
     * 获得当前进程的进程号
     *
     * @return 进程号
     */
    public static int getPid() {
        return Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
    }

    /**
     * 获得当前进程的线程总数
     *
     * @return 当前进程的线程总数
     */
    private static Integer getCurrentThreadCnt(Runtime rt) {
        if (!isNotWindows) {
            throw new RuntimeException("Could Not Run In Windows OS");
        }

        String[] cmd = {
                "/bin/sh",
                "-c",
                "ps -eLf | grep " + getPid() + " | wc -l"
        };

        BufferedReader reader = null;
        try {
            Process process = rt.exec(cmd);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();

            if (line != null) {
                return Integer.valueOf(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    /**
     * 获得系统的环境变量以及当前进程的运行时参数
     */
    public static void systemAnalysis() {
        if (!isNotWindows) {
            throw new RuntimeException("Could Not Run In Windows OS");
        }
        Runtime rt = Runtime.getRuntime();
        SystemEnv.PROCESS_THREAD_CNT = ManagementFactory.getThreadMXBean().getThreadCount();
        BufferedReader in = null;
        try {
            int pid = getPid();
            String[] cmd = {
                    "/bin/sh",
                    "-c",
                    "top -b -n 1 | grep -E \"Cpu\\(s\\)|load\\ average|PID|Mem\\ :|" + pid + "\""
            };
            Process p = rt.exec(cmd);
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String content;
            int lineNum = 1;
            while ((content = in.readLine()) != null) {
                if (lineNum == 1) {
                    loadAverage(content);
                } else if (lineNum == 2) {
                    cpuDetail(content);
                } else if (lineNum == 3) {
                    memoryDetail(content);
                } else if (lineNum == 5) {
                    currentProgressDetail(content);
                }
                lineNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得当前进程的运行时环境
     *
     * @param str -
     */
    private static void currentProgressDetail(String str) {
        String[] strArray;
        int m = 0;
        strArray = str.split(" ");
        for (String info : strArray) {
            if (info.trim().length() == 0) {
                continue;
            }
            if (m == 5) {
                // 第5列为进程占用的物理内存值
                String unit = info.substring(info.length() - 1);
                if (unit.equalsIgnoreCase("g")) {
                    SystemEnv.PROCESS_MEMORY_USED = Double.parseDouble(info.replace("g", "")) * 1024 * 1024;
                } else if (unit.equalsIgnoreCase("m")) {
                    BigDecimal memUseSize = new BigDecimal(info.substring(0, info.length() - 1));
                    SystemEnv.PROCESS_MEMORY_USED = memUseSize.divide(DIVISOR, 2, BigDecimal.ROUND_HALF_UP).doubleValue() * 1024;
                } else if (!"RES".equals(info)) {
                    SystemEnv.PROCESS_MEMORY_USED = Double.valueOf(info);
                }
            }
            if (m == 8) {
                try {
                    // 第9列为CPU的使用百分比
                    SystemEnv.PROCESS_CPU_RATE = Double.parseDouble(info) / 100;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(info);
                }
            }
            if (m == 9) {
                // 第10列为内存的使用百分比
                SystemEnv.PROCESS_MEMORY_PERCENT = Double.parseDouble(info) / 100;
            }
            m++;
        }
    }

    /**
     * 获得机器的运行时内存数据
     */
    private static void memoryDetail(String str) {
        String memPercent = str.substring(str.indexOf(LINUX_MEM_DETAIL_TAG) + LINUX_MEM_DETAIL_TAG.length() + 1);
        if (memPercent.length() > 0) {
            String[] memDetail = memPercent
                    .replaceAll("[a-z /]", "")
                    .split(",");
            SystemEnv.MACHINE_MEM_TOTAL = Long.valueOf(memDetail[0]);
            SystemEnv.MACHINE_MEM_FREE = Long.valueOf(memDetail[1]);
            SystemEnv.MACHINE_MEM_USED = Long.valueOf(memDetail[2]);
            SystemEnv.MACHINE_MEM_BUFF_CACHE = Long.valueOf(memDetail[3]);
        }
    }

    /**
     * 获得当前机器的CPU占比
     */
    private static void cpuDetail(String str) {
        String cpuPercent = str.substring(str.indexOf(LINUX_CPU_PERCENT_TAG) +
                LINUX_CPU_PERCENT_TAG.length() + 1);
        if (cpuPercent.length() > 0) {
            String[] percents = cpuPercent
                    .replaceAll("[a-z ]", "")
                    .split(",");

            SystemEnv.MACHINE_CPU_RATE = Double.valueOf(percents[0]);
        }
    }

    /**
     * 获得当前CPU的负载情况
     *
     * @param str
     */
    private static void loadAverage(String str) {
        String loadAverage = str.substring(str.indexOf(LINUX_CPU_LOAD_AVERAGE_TAG) +
                LINUX_CPU_LOAD_AVERAGE_TAG.length() + 1);
        if (loadAverage.length() > 0) {
            String[] laVal = loadAverage.replaceAll(" ", "").split(",");
            if (laVal.length == 3) {
                SystemEnv.MACHINE_LA1 = Double.valueOf(laVal[0]);
                SystemEnv.MACHINE_LA2 = Double.valueOf(laVal[1]);
                SystemEnv.MACHINE_LA3 = Double.valueOf(laVal[2]);
            }
        }
    }

}  