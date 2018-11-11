/**
 * Project: x-framework
 * Package Name: org.ike.core
 * Author: Xuejia
 * Date Time: 2016/11/18 20:23
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base;

import org.apache.zookeeper.ZooKeeper;
import org.chim.altass.base.config.XmlConfiguration;
import org.chim.altass.base.zookeeper.KeeperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class Name: AppInitialization
 * Create Date: 2016/11/18 20:23
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * 应用启动监听器
 * 用于注册应用环境
 */
public class AppInitialization implements ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(AppInitialization.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // 应用创建的时候，将自动向zookeeper系统节点下的app节点注册相关的信息
        ServletContext context = servletContextEvent.getServletContext();
        String appName = context.getInitParameter("APP_NAME");

        // 如果应用的名称是FRAMEWORK-CORE，则说明当前启动的应用是核心应用
        if (appName.contains("FRAMEWORK-CORE")) {
            // 需要检测核心节点是否存在，如果存在，那么进行状态更新操作，如果不存在，那么需要创建一个节点表示当前核心应用已经启动
//            prepareCoreInit(servletContextEvent, appName);
        } else {

        }
        System.err.println("正在获取当前应用的名称，即将注册到zookeeper中，使其成为一个节点");
    }

    /**
     * 初始化zookeeper节点
     *
     * @param servletContextEvent -
     * @param appName             当前启动的核心引用的名称
     */
    private void prepareCoreInit(ServletContextEvent servletContextEvent, String appName) {
        // 判断当前的zookeeper是否启用，如果启用了，那么由zookeeper管理所有的配置内容
        XmlConfiguration xmlConfiguration = XmlConfiguration.getInstance(servletContextEvent.getServletContext());
        HashMap<String, Object> configMap = xmlConfiguration.getConfigMap();

        xmlConfiguration.getConfigMap().put(XmlConfiguration.CONFIG_K_FRAMEWORK_CORE_APP_NAME, appName);
        String zookeeperIp;
        if ((zookeeperIp = String.valueOf(configMap.get(XmlConfiguration.CONFIG_K_ZOOKEEPER_IP))) != null) {
            // 判断当前是否启用了zookeeper配置，如果启用了相应的配置信息，那么直接启动一个zookeeper客户端连接并且写入一个框架
            // 级的节点信息到对应IP的节点中
            String zookeeperPort = (String) configMap.get(XmlConfiguration.CONFIG_K_ZOOKEEPER_PORT);
            try {
                logger.info("框架核心正在准备初始化Zookeeper节点数据[ip = " + zookeeperIp + ", port = " + zookeeperPort + "] ...");
                ZooKeeper zk = KeeperUtils.createNewKeeper(zookeeperIp, zookeeperPort,
                        watchedEvent -> logger.info("框架核心侦测到应用集群配置发生变化:" + watchedEvent.toString()));

                String appNode = "/" + appName;
                // 创建一个会话及别的核心应用存活节点
                KeeperUtils.createSession(appNode + "_alived", System.currentTimeMillis(), zk);
                // 创建一个永久有效的核心应用节点
                KeeperUtils.createPersistent(appNode, "{\"zookeeperIp\":\"" + zookeeperIp + "\", \"zookeeperPort\":" + zookeeperPort + "}", zk);
                // 创建应用节点
                KeeperUtils.createPersistent(appNode + "/Apps", "{\"appCount\": 0}", zk);
                // 创建服务节点
                KeeperUtils.createPersistent(appNode + "/Service", "{\"servCount\":0}", zk);
                // 创建系统配置节点
                KeeperUtils.createPersistent(appNode + "/Sys", "{}", zk);

                logger.info("框架核心初始化Zookeeper节点数据完成");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.err.println("将注册的应用注销");
    }
}
