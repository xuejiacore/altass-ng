/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.centers.config
 * Author: Xuejia
 * Date Time: 2016/12/21 9:46
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.manager;


import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.utils.type.ClassUtil;
import org.chim.altass.core.configuration.NodeResource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class Name: NodeResourceManager
 * Create Date: 2016/12/21 9:46
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 节点资源管理器
 */
public class NodeResourceManager {

    /**
     * 节点资源管理器实例
     */
    private static NodeResourceManager manager = null;
    /**
     * 节点资源配置
     */
    private static LinkedHashMap<String, NodeResource> resourceMap = null;
    private static LinkedHashMap<String, NodeResource> resourceMapId = null;

    /**
     * 节点的组配置
     */
    private static Map<String, List<NodeResource>> groupMap = null;
    /**
     * 节点资源的配置路径集合
     */
    private static List<String> configList = null;

    /**
     * 获得节点资源管理器实例
     *
     * @return 节点资源管理器实例
     */
    public static NodeResourceManager getInstance() {
        if (manager == null) {
            synchronized (NodeResourceManager.class) {
                if (manager == null) {
                    manager = new NodeResourceManager();
                }
            }
        }
        return manager;
    }

    /**
     * 创建一个节点资源管理器
     */
    private NodeResourceManager() {
        resourceMap = new LinkedHashMap<>();
        resourceMapId = new LinkedHashMap<>();
        configList = new ArrayList<>();
        groupMap = new ConcurrentHashMap<>();
    }

    public static void main(String[] args) throws XmlParserException {
        ControlCenter.CenterProperty property = new ControlCenter.CenterProperty();
        property.setNodeResourceManagerXmlPath("C:\\JavaProjects\\其他项目\\x-framework\\libraries\\x-eurekacore\\src\\main\\resources\\eurekacore-site.xml");
        property.setNodeResourceManagerPkgPath("org.chim.altass.core.executor;org.ike.eurekacore.core.domain.node;org.chim.altass.eurekacore.ext");
        // 使用配置装配所有的中心组件
        ControlCenter.newInstance(property);
        System.err.println(ControlCenter.getInstance().getNodeResourceManager().getResourceMap());

    }

    /**
     * 加载配置文件
     *
     * @param configPath 配置文件的路径
     */
    public void loadConfiguration(String configPath, String packagePath, String webDir) throws XmlParserException {
        String[] configPaths = configPath.split(";");
        for (String filePath : configPaths) {

            // 将当前的加载路径缓存到缓存列表中
            configList.add(filePath);
            String preDir = webDir + (webDir.endsWith("/") ? "" : "/");
            String[] packagePaths = packagePath.split(";");

            // 只有允许组装的节点才被纳入到节点资源管理器中
            for (String path : packagePaths) {
                // 查找对应包路径下的所有节点信息，将查找到的节点配置到节点资源中
                Set<Class<?>> classes = ClassUtil.getClasses(path);
                for (Class<?> aClass : classes) {
                    Executable executable = aClass.getAnnotation(Executable.class);
                    Resource res = aClass.getAnnotation(Resource.class);
                    if (executable != null && executable.assemble()) {
                        if (res != null && res.clazz().equals(aClass)) {
                            String key = res.clazz().getName();
                            NodeResource nodeResource = new NodeResource();
                            nodeResource.setConfigUrl(res.pageUrl());                                           // 配置页面URL
                            nodeResource.setGroupName(res.groupName());                                         // 资源分组
                            nodeResource.setNodeName(res.name());                                               // 节点名称
                            nodeResource.setSmallImage(preDir + res.smallImage());                              // 节点小图
                            nodeResource.setMidImage(preDir + res.midImage());                                  // 节点中图
                            nodeResource.setBigImage(preDir + res.bigImage());                                  // 节点大图
                            nodeResource.setClazz(res.getClass().getName());                                    // 执行器类名
                            nodeResource.setClazz(res.clazz());                                                 // 执行器类
                            nodeResource.setAbility(String.join(",", executable.ability()));         // 节点能力

                            resourceMap.put(key, nodeResource);
                            resourceMapId.put(nodeResource.getResId(), nodeResource);

                            // 判断组名
                            key = nodeResource.getGroupName();
                            List<NodeResource> nodeResources;
                            if ((nodeResources = groupMap.get(key)) == null) {
                                nodeResources = new ArrayList<>();
                            }
                            nodeResources.add(nodeResource);
                            groupMap.put(key, nodeResources);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获得节点资源映射信
     *
     * @return 获得资源的映射数据
     */
    public Map<String, NodeResource> getResourceMap() {
        return resourceMap;
    }

    /**
     * 根据节点的类型获取节点资源的情况
     *
     * @param nodeClz 节点的类
     * @return 节点的资源数据
     */
    public NodeResource getResource(Class<?> nodeClz) {
        return getResource(nodeClz.getName());
    }

    /**
     * 根据节点的资源ID获得节点的资源配置数据
     *
     * @param resId 资源ID
     * @return 节点的资源配置数据
     */
    public NodeResource getResourceById(String resId) {
        return resourceMapId.get(resId);
    }

    /**
     * 根据节点的类型名称获得节点的资源情况
     *
     * @param key 节点的类名称（包含包路径）
     * @return 节点的资源数据
     */
    public NodeResource getResource(String key) {
        return resourceMap.get(key);
    }

    /**
     * 获得节点组资源
     *
     * @return 节点组资源
     */
    public Map<String, List<NodeResource>> getGroupMap() {
        return groupMap;
    }

    /**
     * 根据组名获得节点组资源配置
     *
     * @param key 组名称
     * @return 同组节点组配置
     */
    public List<NodeResource> getGroupResource(String key) {
        return groupMap.get(key);
    }

}
