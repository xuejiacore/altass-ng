package org.chim.altass.core.manager.monitor;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.chim.altass.core.constant.SystemEnv;
import org.chim.altass.core.constant.SystemEnvMonitor;
import org.chim.altass.toolkit.JedisToolkit;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.IOException;
import java.util.*;

/**
 * Class Name: Heartbeats
 * Create Date: 18-2-7 下午10:51
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 监视心跳，实现了信号捕捉接口，用于优雅停机
 */
public abstract class HealthMonitor implements SignalHandler, Watcher {

    protected Logger logger = Logger.getLogger(super.getClass());

    private static Integer SYS_MONITOR_CHECK_DURATION = 1000;

    private static final String ROOT = "/eureka";                                       // 服务在zk上的注册目录

    private static final String SERVICE_NODE = ROOT + "/services";                      // 服务数据节点

    private static final String SESSION_NODE = SERVICE_NODE + "/session";               // 服务的会话节点

    private static final String NODE_NAME = SystemEnv.SERVICE_NODE_NAME;                // 节点名称

    private static final String SERVICE_NODE_DIR = SERVICE_NODE + "/" + NODE_NAME;      // 服务节点的数据目录

    private static ZooKeeper zooKeeper = null;
    private static final Set<String> GLOBAL_SERVICE_NODE_SET = new HashSet<String>();

    /**
     * 创建并实例化，初始化监听 TERM 信号量
     */
    public HealthMonitor() {
        Signal.handle(new Signal("TERM"), this);
    }

    /**
     * 载入服务，向集群注册服务
     */
    public void launchService() {
        // 设置当前节点的全局节点目录环境
        SystemEnv.SERVICE_NODE_DIR = SERVICE_NODE_DIR;
        logger.trace("Registering Cluster Healthy Monitor.");
        try {
            zooKeeper = new ZooKeeper(SystemEnv.ZOOKEEPER_QUORUM, 10000, this);

            Stat exists = zooKeeper.exists(ROOT, null);
            if (exists == null) {
                zooKeeper.create(ROOT, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                exists = zooKeeper.exists(SERVICE_NODE, null);
                if (exists == null) {
                    zooKeeper.create(SERVICE_NODE, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            }

            // 此处会建立当前节点的目录，用于存储分布式的节点运行信息
            String timestamp = System.currentTimeMillis() + "";
            if (zooKeeper.exists(SERVICE_NODE_DIR, null) == null) {
                zooKeeper.create(SERVICE_NODE_DIR, timestamp.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            // TODO: 启动完成后，需要检查上一次停止的时候是否有未完成的节点或作业，如果有，在启动的时候检查标记并进行恢复
            // TODO: 启动完成后，需要检查上一次停止的时候是否有未完成的节点或作业，如果有，在启动的时候检查标记并进行恢复


            if (zooKeeper.exists(SESSION_NODE, null) == null) {
                zooKeeper.create(SESSION_NODE, timestamp.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            String sessionNode = SESSION_NODE + "/" + NODE_NAME;
            if (zooKeeper.exists(sessionNode, null) == null) {
                zooKeeper.create(sessionNode, timestamp.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            } else {
                logger.error("Service named [" + SystemEnv.SERVICE_NODE_ID + "] had exists on [" + SystemEnv.SERVICE_NODE_HOST + "], stopping...");
                System.exit(0);
            }

        } catch (IOException | InterruptedException | KeeperException e) {
            e.printStackTrace();
        }

        this.initSystemMonitor(SYS_MONITOR_CHECK_DURATION);
    }

    /**
     * 初始化系统环境监视器
     *
     * @param checkDuration 检查周期
     */
    private void initSystemMonitor(Integer checkDuration) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 定时获得当前节点的系统变量
                SystemInfoTools.systemAnalysis();
                JedisToolkit toolkit = JedisToolkit.getInstance();
                toolkit.zadd(SystemEnvMonitor.MONICA$SYS_ENV_MACHINE_CPU_RATE, 0, SystemEnv.MACHINE_CPU_RATE, SystemEnv.SERVICE_NODE_ID);
                toolkit.zadd(SystemEnvMonitor.MONICA$SYS_ENV_MACHINE_MEM_TOTAL, 0, SystemEnv.MACHINE_MEM_TOTAL * 1.0, SystemEnv.SERVICE_NODE_ID);
                toolkit.zadd(SystemEnvMonitor.MONICA$SYS_ENV_MACHINE_MEM_FREE, 0, SystemEnv.MACHINE_MEM_FREE * 1.0, SystemEnv.SERVICE_NODE_ID);
                toolkit.zadd(SystemEnvMonitor.MONICA$SYS_ENV_MACHINE_MEM_USED, 0, SystemEnv.MACHINE_MEM_USED * 1.0, SystemEnv.SERVICE_NODE_ID);
                toolkit.zadd(SystemEnvMonitor.MONICA$SYS_ENV_MACHINE_MEM_BUFF_CACHE, 0, SystemEnv.MACHINE_MEM_BUFF_CACHE * 1.0, SystemEnv.SERVICE_NODE_ID);
                toolkit.zadd(SystemEnvMonitor.MONICA$SYS_ENV_MACHINE_LA1, 0, SystemEnv.MACHINE_LA1, SystemEnv.SERVICE_NODE_ID);
                toolkit.zadd(SystemEnvMonitor.MONICA$SYS_ENV_MACHINE_LA2, 0, SystemEnv.MACHINE_LA2, SystemEnv.SERVICE_NODE_ID);
                toolkit.zadd(SystemEnvMonitor.MONICA$SYS_ENV_MACHINE_LA3, 0, SystemEnv.MACHINE_LA3, SystemEnv.SERVICE_NODE_ID);
                toolkit.zadd(SystemEnvMonitor.MONICA$SYS_ENV_PROCESS_CPU_RATE, 0, SystemEnv.PROCESS_CPU_RATE, SystemEnv.SERVICE_NODE_ID);
                toolkit.zadd(SystemEnvMonitor.MONICA$SYS_ENV_PROCESS_MEMORY_USED, 0, SystemEnv.PROCESS_MEMORY_USED, SystemEnv.SERVICE_NODE_ID);
                toolkit.zadd(SystemEnvMonitor.MONICA$SYS_ENV_PROCESS_MEMORY_PERCENT, 0, SystemEnv.PROCESS_MEMORY_PERCENT, SystemEnv.SERVICE_NODE_ID);
                toolkit.zadd(SystemEnvMonitor.MONICA$SYS_ENV_PROCESS_THREAD_CNT, 0, SystemEnv.PROCESS_THREAD_CNT * 1.0, SystemEnv.SERVICE_NODE_ID);
            }
        }, checkDuration, checkDuration);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        boolean needToWatch = true;
        String eventPath = watchedEvent.getPath();
        try {
            if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                zooKeeper.exists(eventPath, this);
                logger.trace("Node [" + eventPath + "] had been killed.");
                this.onServiceDie(SystemEnv.SERVICE_NODE_ID);
                needToWatch = false;
            } else if (watchedEvent.getType() == Event.EventType.NodeCreated) {
                logger.trace("Node [" + eventPath + "] had been restore.");
                zooKeeper.exists(eventPath, this);
                this.onServiceRestore(SystemEnv.SERVICE_NODE_ID);
                needToWatch = false;
            } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged && watchedEvent.getPath().equals(SERVICE_NODE)) {
                needToWatch = true;
            }
            List<String> children = zooKeeper.getChildren(SESSION_NODE, this);
            if (needToWatch) {
                for (String child : children) {
                    String path = SESSION_NODE + "/" + child;
                    if (GLOBAL_SERVICE_NODE_SET.add(path)) {
                        logger.trace(path + " had been registered for the first time.");
                        zooKeeper.exists(path, this);
                    }
                }
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO:异常宕机的节点恢复后，对于是本地io的节点，进行原地恢复
     *
     * @param name 节点名称
     */
    protected abstract void onServiceRestore(String name);

    /**
     * TODO:检测到某一个服务节点异常宕机，需要检查当前节点是否有部分执行器在问题节点中，是否是不可迁移节点（非本地io），对应进行
     * TODO:本地恢复或者是执行转移
     * <p>
     * TODO:但是必须的一点是，涉及的作业都必须暂停执行
     *
     * @param name 节点名称
     */
    protected abstract void onServiceDie(String name);

    /**
     * 作业节点被使用 kill 命令停止时触发
     * <p>
     * 在非 kill -9 命令杀掉的服务才能够通过这种方式进行优雅停机，如果是因为 kill -9 强杀或者因为机器断电或宕机导致的服务停止，由非
     * 中心心跳来监视服务节点停机，需要注意的是，如果因为强杀或者机器断电或宕机导致的服务停止，由于服务无法及时地停止以及现场封存下来，
     * 在下次重启恢复的时候可能会出现少量数据确实断链的问题（特别是实时性特别高的作业，对于走管道流的（内部实现是一个消息队列），这种突
     * 然的停机影响不大）
     */
    public abstract void onStopping();

    /**
     * handler 处理 kill 命令（不包括-9）
     * 用于优雅停机的实现
     *
     * @param signal signal
     */
    @Override
    public void handle(Signal signal) {
        if ("TERM".equals(signal.getName())) {
            try {
                zooKeeper.delete(SERVICE_NODE + "/" + NODE_NAME, -1);
            } catch (InterruptedException | KeeperException e) {
                e.printStackTrace();
            }
            onStopping();
        }
    }

}
