package org.chim.altass.core.manager.monitor;

/**
 * Class Name: DefaultHealthMonitor
 * Create Date: 18-2-9 下午1:37
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class DefaultHealthMonitor extends HealthMonitor {

    /**
     * @param name 节点名称
     */
    @Override
    protected void onServiceRestore(String name) {

    }

    /**
     * @param name 节点名称
     */
    @Override
    protected void onServiceDie(String name) {

    }

    @Override
    public void onStopping() {
        System.err.println("----服务停机----");
        // TODO:停机时，最先查询出当前服务正在运行的节点有哪些

        // 向这些节点发送状态保存信号，同时向这些作业的直接前驱后继发送暂停信号，主动切断直接前驱后继的数据传输与作业运作

        // TODO：遍历标记运行池中的所有作业节点为停摆状态（可被ui服务查询出来）

        System.exit(0);
    }
}
