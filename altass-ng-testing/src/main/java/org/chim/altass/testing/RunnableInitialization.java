/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore
 * Author: Xuejia
 * Date Time: 2016/12/15 15:04
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.testing;


import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.EXmlParser;
import org.chim.altass.core.configuration.*;
import org.chim.altass.core.executor.general.*;
import org.chim.altass.core.executor.io.FileCreateExecutor;
import org.chim.altass.core.executor.io.FileExistedChecker;

import java.io.IOException;

/**
 * Class Name: RunnableInitialization
 * Create Date: 2016/12/15 15:04
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 可运行ETL初始化配置生成器
 */
public class RunnableInitialization {
    private static final int CONFIG_ETL_SITE = 0x01;

    public static void main(String[] args) throws XmlParserException, IOException {
        switch (CONFIG_ETL_SITE) {
            case CONFIG_ETL_SITE:
                // 创建ETL配置文件
                createEtlSite();
                break;
        }
    }

    private static void createEtlSite() throws XmlParserException, IOException {
        // 创建一个ETL-SITE配置
        EurekaSiteConfiguration eurekaSiteConfiguration = new EurekaSiteConfiguration();

        // ------------------------>>  1、初始化节点资源配置
        NodeResources resources = new NodeResources();
        eurekaSiteConfiguration.setReleaseMode("Beta");                        // 设置发布模式
        eurekaSiteConfiguration.setVersion("0.0.1");                           // 设置版本号

        // 分组开始
        // // 创建默认分组
        NodeGroup defaultGroup = new NodeGroup("常规节点");
        NodeResource nodeResource = null;


        nodeResource = new NodeResource("子作业", "常规节点", "res/image/support/nodeflow/bgs/job_bg.png", JobExecutor.class);
        nodeResource.setConfigUrl("http://www.baidu.com");
        defaultGroup.addNodeResource(nodeResource);

        nodeResource = new NodeResource("延时器", "常规节点", "res/image/support/nodeflow/bgs/delay_bg.png", TimerExecutor.class);
        nodeResource.setConfigUrl("nodeConfigs/general/timerNodeConfig.jsp");
        defaultGroup.addNodeResource(nodeResource);

        nodeResource = new NodeResource("文件存在性", "常规节点", "res/image/support/nodeflow/bgs/file_existed_bg.png", FileExistedChecker.class);
        nodeResource.setConfigUrl("nodeConfigs/general/timerNodeConfig.jsp");
        defaultGroup.addNodeResource(nodeResource);

        nodeResource = new NodeResource("多路归并", "常规节点", "res/image/support/nodeflow/bgs/aggregation_bg.png", SimpleBlockingExecutor.class);
        nodeResource.setConfigUrl("http://www.baidu.com");
        defaultGroup.addNodeResource(nodeResource);

//        nodeResource = new NodeResource("Shell", "常规节点", "res/image/support/nodeflow/bgs/shell_bg.png", ShellExecutor.class);
//        nodeResource.setConfigUrl("http://www.baidu.com");
//        defaultGroup.addNodeResource(nodeResource);

//        nodeResource = new NodeResource("Http", "常规节点", "res/image/support/nodeflow/bgs/http_bg.png", HttpExecutor.class);
//        nodeResource.setConfigUrl("http://www.baidu.com");
//        defaultGroup.addNodeResource(nodeResource);
//
//        nodeResource = new NodeResource("Flume", "常规节点", "res/image/support/nodeflow/bgs/flume_bg.png", FlumeExecutor.class);
//        nodeResource.setConfigUrl("http://www.baidu.com");
//        defaultGroup.addNodeResource(nodeResource);
//
//        nodeResource = new NodeResource("持久化", "常规节点", "res/image/support/nodeflow/bgs/database_bg.png", PersistenceExecutor.class);
//        nodeResource.setConfigUrl("http://www.baidu.com");
//        defaultGroup.addNodeResource(nodeResource);
//
//        nodeResource = new NodeResource("Redis", "常规节点", "res/image/support/nodeflow/bgs/redis_bg.png", RedisExecutor.class);
//        nodeResource.setConfigUrl("http://www.baidu.com");
//        defaultGroup.addNodeResource(nodeResource);
//
//        nodeResource = new NodeResource("RPC", "常规节点", "res/image/support/nodeflow/bgs/rpc_interface_bg.png", RpcExecutor.class);
//        nodeResource.setConfigUrl("http://www.baidu.com");
//        defaultGroup.addNodeResource(nodeResource);
//
//        nodeResource = new NodeResource("消息", "常规节点", "res/image/support/nodeflow/bgs/sys_msg_bg.png", SysMsgExecutor.class);
//        nodeResource.setConfigUrl("http://www.baidu.com");
//        defaultGroup.addNodeResource(nodeResource);
//
//        nodeResource = new NodeResource("Maven", "常规节点", "res/image/support/nodeflow/bgs/maven_bg.png", MavenExecutor.class);
//        nodeResource.setConfigUrl("http://www.baidu.com");
//        defaultGroup.addNodeResource(nodeResource);

        nodeResource = new NodeResource("触发事件", "常规节点", "res/image/support/nodeflow/bgs/trigger_bg.png", SimpleTriggerExecutor.class);
        nodeResource.setConfigUrl("http://www.baidu.com");
        defaultGroup.addNodeResource(nodeResource);

        nodeResource = new NodeResource("事件监听", "常规节点", "res/image/support/nodeflow/bgs/event_bg.png", SimpleEventExecutor.class);
        nodeResource.setConfigUrl("http://www.baidu.com");
        defaultGroup.addNodeResource(nodeResource);

        // ---------------

        nodeResource = new NodeResource("开始节点", "常规节点", "res/image/support/nodeflow/bgs/node-start.png", StartExecutor.class);
        nodeResource.setConfigUrl("nodeConfigs/general/startNodeConfig.jsp");
        defaultGroup.addNodeResource(nodeResource);

        nodeResource = new NodeResource("结束节点", "常规节点", "res/image/support/nodeflow/bgs/node-end.png", EndExecutor.class);
        nodeResource.setConfigUrl("http://www.baidu.com");
        defaultGroup.addNodeResource(nodeResource);

        resources.addNodeGroup(defaultGroup);


        // // 创建文件操作分组
        NodeGroup fileGroup = new NodeGroup("文件操作");
        nodeResource = new NodeResource("创建文件夹", "文件操作", "res/image/support/nodeflow/bgs/createFolder_bg.png", FileCreateExecutor.class);
        nodeResource.setConfigUrl("http://www.baidu.com");
        fileGroup.addNodeResource(nodeResource);

        resources.addNodeGroup(fileGroup);

        eurekaSiteConfiguration.setResources(resources);
        eurekaSiteConfiguration.setResources(resources);

        // 配置日志
        LogConfig logConfig = new LogConfig();
        logConfig.setExecutorLogDir("/data/eureka/logs/exe/");
        logConfig.setSystemLogDir("/data/eureka/logs/sys/");
        eurekaSiteConfiguration.setLogConfig(logConfig);

        EXmlParser.writeTo(eurekaSiteConfiguration, GlobalVars.ETL_SITE_CONFIG_PATH);
        // ETL-SITE配置结束
        EurekaSiteConfiguration siteConfiguration = EXmlParser.readFrom(GlobalVars.ETL_SITE_CONFIG_PATH, EurekaSiteConfiguration.class);

        // ------------------------>>  2、
    }
}
