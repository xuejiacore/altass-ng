/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.centers
 * Author: Xuejia
 * Date Time: 2016/12/21 17:36
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.manager;


import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.core.configuration.GlobalVars;
import org.chim.altass.core.log.ILogger;
import org.chim.altass.core.log.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class Name: ControlCenter
 * Create Date: 2016/12/21 17:36
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 控制中心
 */
public class ControlCenter {
    private ILogger logger = LogFactory.getInstance(ControlCenter.class);
    /**
     * 控制中心单例对象
     */
    private static ControlCenter controlCenter = null;

    /**
     * 节点资源管理中心组件
     */
    public static final Integer CENTER_NODE_RESOURCE_MANAGER = 0x01;
    public static final Integer CENTER_NODE_HEL_MONITOR_MANAGER = 0x02;
    /**
     * 控制中心组件集合
     */
    private Map<Integer, Object> centers = null;

    /**
     * 获得控制中心实例
     *
     * @return 控制中心实例
     */
    public static ControlCenter getInstance() {
        if (controlCenter == null) {
            try {
                throw new Exception("必须先对控制中心进行初始化");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return controlCenter;
    }

    /**
     * TODO:测试使用默认装配中心
     */
    public static void testInitialization(String prefixDir) {
        CenterProperty property = new CenterProperty();
        property.setNodeResourceManagerXmlPath(GlobalVars.ETL_SITE_CONFIG_PATH);
        property.setNodeResourceManagerPkgPath(
                "org.chim.corelab.eurekacore.core.executor;" +
                        "org.chim.corelab.eurekacore.core.domain.buildin;" +
                        "org.chim.corelab.eurekacore.ext;"
        );
        property.setResourceManagerPrefixDir(prefixDir);
        // 使用配置装配所有的中心组件
        ControlCenter.newInstance(property);
    }

    /**
     * 根据控制中心的配置获得控制中心实例
     *
     * @param property 控制中心的配置
     * @return 控制中心实例
     */
    public static ControlCenter newInstance(CenterProperty property) {
        if (controlCenter == null) {
            synchronized (ControlCenter.class) {
                if (controlCenter == null) {
                    try {
                        controlCenter = new ControlCenter(property);
                    } catch (XmlParserException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return controlCenter;
    }

    /**
     * 初始化控制中心
     */
    private ControlCenter() {

    }

    /**
     * 初始化配置
     *
     * @param property 初始化配置的配置属性
     */
    private ControlCenter(CenterProperty property) throws XmlParserException {
        centers = new ConcurrentHashMap<>();
        // 初始化节点资源管理中心
        String nodeResourceManagerXmlPath = property.getNodeResourceManagerXmlPath();
        String nodeResourceManagerPkgPath = property.getNodeResourceManagerPkgPath();
        String nodeResourceManagerPrefixDir = property.getResourceManagerPrefixDir();
        if (nodeResourceManagerXmlPath != null && nodeResourceManagerPkgPath != null) {
            logger.info("+- - - -+ 装配节点资源 开始");
            NodeResourceManager nodeResourceManager = NodeResourceManager.getInstance();
            nodeResourceManager.loadConfiguration(nodeResourceManagerXmlPath, nodeResourceManagerPkgPath, nodeResourceManagerPrefixDir);
            centers.put(CENTER_NODE_RESOURCE_MANAGER, nodeResourceManager);
            logger.info("+- - - -+ 节点资源装配 完成");
        }


    }

    /**
     * 获得控制中心的所有中心组件
     *
     * @return 控制中心的所有中心组件
     */
    public Map<Integer, Object> getCenters() {
        return centers;
    }

    /**
     * 根据中心组件的ID获得中心组件
     *
     * @param centerId 中心组件的ID，ControlCenter常量
     * @return 中心组件实例
     */
    public Object getCenter(Integer centerId) {
        return centers.get(centerId);
    }

    /**
     * 获得节点资源管理器实例
     *
     * @return 节点资源管理器实例
     */
    public NodeResourceManager getNodeResourceManager() {
        return (NodeResourceManager) centers.get(CENTER_NODE_RESOURCE_MANAGER);
    }


    /**
     * 中心控制的属性配置
     */
    public static class CenterProperty {
        private String nodeResourceManagerXmlPath = null;
        private String nodeResourceManagerPkgPath = null;
        private String resourceManagerPrefixDir = null;

        public String getNodeResourceManagerXmlPath() {
            return nodeResourceManagerXmlPath;
        }

        public void setNodeResourceManagerXmlPath(String nodeResourceManagerXmlPath) {
            this.nodeResourceManagerXmlPath = nodeResourceManagerXmlPath;
        }

        public String getNodeResourceManagerPkgPath() {
            return nodeResourceManagerPkgPath;
        }

        public void setNodeResourceManagerPkgPath(String nodeResourceManagerPkgPath) {
            this.nodeResourceManagerPkgPath = nodeResourceManagerPkgPath;
        }

        public void setResourceManagerPrefixDir(String resourceManagerPrefixDir) {
            this.resourceManagerPrefixDir = resourceManagerPrefixDir;
        }

        public String getResourceManagerPrefixDir() {
            return this.resourceManagerPrefixDir;
        }
    }

}
