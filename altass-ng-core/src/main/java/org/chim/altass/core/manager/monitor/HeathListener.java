package org.chim.altass.core.manager.monitor;


import org.chim.altass.core.constant.SystemEnv;

/**
 * Class Name: HeathListener
 * Create Date: 18-2-7 下午11:38
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 节点运行状态信息
 */
public interface HeathListener {

    /**
     * 节点启动回调
     * <p>
     * 节点启动，主动注册的同时广播节点启动信息到集群中的其他节点，用于其他节点感知新节点启动
     *
     * @param info 节点信息
     */
    void onNodeLaunch(SystemEnv info);

    /**
     * 节点心跳信息
     *
     * @param info 心跳信息
     */
    void echo(EchoInfo info);

    /**
     * 节点判定停机回调
     *
     * @param info 停机回调信息
     */
    void onNodeDeath(SystemEnv info);

}
