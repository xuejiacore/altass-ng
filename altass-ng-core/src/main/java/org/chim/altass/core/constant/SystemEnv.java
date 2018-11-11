package org.chim.altass.core.constant;

/**
 * Class Name: SystemEnv
 * Create Date: 18-2-24 下午8:02
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class SystemEnv {

    public static final String SERVICE_NODE_ID;                             // 节点的id
    public static String SERVICE_NODE_HOST = "127.0.0.1";                   // 节点的ip
    public static String SERVICE_NODE_NAME = "undefined";                   // 集群服务节点的节点名称，会被用于节点存活监控
    public static String ZOOKEEPER_QUORUM = "127.0.0.1:2181";               // zookeeper集群的配置
    public static String SERVICE_NODE_DIR = "/eureka/services/default";     // 节点的数据目录，默认为default，会在服务启动的时候设置对应的值

    // 运行环境参数
    public static Double MACHINE_CPU_RATE = null;                           // 系统CPU使用率
    public static Long MACHINE_MEM_TOTAL = null;                            // 系统物理内存总量
    public static Long MACHINE_MEM_FREE = null;                             // 系统空闲的物理内存总量
    public static Long MACHINE_MEM_USED = null;                             // 系统使用的物理内存总量
    public static Long MACHINE_MEM_BUFF_CACHE = null;                       // 系统用作内核缓存的内存量
    public static Double MACHINE_LA1 = null;                                // 系统CPU近1分钟平均负载
    public static Double MACHINE_LA2 = null;                                // 系统CPU近5分钟平均负载
    public static Double MACHINE_LA3 = null;                                // 系统CPU近15分钟平均负载

    public static Double PROCESS_CPU_RATE = null;                           // 当前进程的CPU使用率
    public static Double PROCESS_MEMORY_USED = null;                        // 当前进程的内存使用量
    public static Double PROCESS_MEMORY_PERCENT = null;                     // 当前线程的内存占比
    public static Integer PROCESS_THREAD_CNT = null;                        // 当前进程的线程总数

    static {
        SERVICE_NODE_ID = System.getProperty("eureka.service.id", "default");
    }
}
