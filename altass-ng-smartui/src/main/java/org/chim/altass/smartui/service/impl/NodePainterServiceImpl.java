package org.chim.altass.smartui.service.impl;
/*
 * Project: x-framework
 * Package Name: PACKAGE_NAME
 * Author: Xuejia
 * Date Time: 2017/2/7 17:36
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */

import com.alibaba.fastjson.JSON;
import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.EXmlParser;
import org.chim.altass.core.AltassNode;
import org.chim.altass.core.configuration.NodeResource;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.Node;
import org.chim.altass.core.domain.buildin.entry.EndEntry;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.domain.buildin.entry.StartEntry;
import org.chim.altass.core.domain.connector.Connector;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.general.JobExecutor;
import org.chim.altass.core.manager.ControlCenter;
import org.chim.altass.core.manager.NodeResourceManager;
import org.chim.altass.smartui.service.INodeOPService;
import org.chim.altass.smartui.service.INodePainterService;
import org.chim.altass.toolkit.JDFWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Name: org.ike.etl.assemble.core.NodePainterServiceImpl
 * Create Date: 2017/2/7 17:36
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Service("nodePainterService")
public class NodePainterServiceImpl implements INodePainterService {

    private static final String KEY_START_NODE = "START_NODE";              // 开始节点
    private static final String KEY_END_NODE = "END_NODE";                  // 结束节点
    private static final String KEY_PRE_LOAD_NODES = "PRE_LOAD_NODES";      // 预加载节点集合
    private static final String KEY_NODE_PANEL = "NODE_PANEL";              // 节点面板
    private static final String KEY_JOB_ID = "KEY_JOB_ID";                  // 作业ID

    // TODO:临时使用文件存储的形式存储作业信息
    private INodeOPService nodeOPService = new TempNodeOPServiceImpl();

    @Autowired
    private AltassNode altassNode = null;

    @Override
    public HashMap<String, Object> initPainter() {
        // TODO：使用测试的实例初始化方法
        ControlCenter.testInitialization("/data/");
        HashMap<String, Object> configurations = new HashMap<>();
        Map<String, Node> preLoadNodes = new HashMap<>();
        Job job = new Job();
        // TODO：初始化节点面板的时候，自动创建开始节点以及结束节点
        StartEntry startNode = new StartEntry(job, 400, 300);
        EndEntry endNode = new EndEntry(job, 900, 300);
        job.setExecutorClz(JobExecutor.class);
        nodeOPService.createJob(job);
        preLoadNodes.put(KEY_START_NODE, startNode);
        preLoadNodes.put(KEY_END_NODE, endNode);

        configurations.put(KEY_PRE_LOAD_NODES, preLoadNodes);
        configurations.put(KEY_JOB_ID, job.getNodeId());

        // TODO：初始化工具面板中的可用节点情况，需要将对应节点类型的分组以及资源情况进行展示
        configurations.put(KEY_NODE_PANEL, ControlCenter.getInstance().getNodeResourceManager().getResourceMap());
        return configurations;
    }

    /**
     * 获得节点资源
     *
     * @return 节点资源信息
     */
    @Override
    public Map<String, List<NodeResource>> obtainNodeResource() {
        NodeResourceManager nodeResourceManager = ControlCenter.getInstance().getNodeResourceManager();
        return nodeResourceManager.getGroupMap();
    }

    /**
     * 获得资源信息
     *
     * @return 资源信息列表
     */
    @Override
    public List<NodeResource> obtainResources() {
        NodeResourceManager nodeResourceManager = ControlCenter.getInstance().getNodeResourceManager();
        Map<String, NodeResource> resourceMap = nodeResourceManager.getResourceMap();
        List<NodeResource> resourcesList = new ArrayList<>();
        resourcesList.addAll(resourceMap.values());
        return resourcesList;
    }

    /**
     * 根据资源的ID获得资源
     *
     * @param resId 资源ID
     * @return 资源配置数据
     */
    @Override
    public NodeResource obtainResource(String resId) {
        NodeResourceManager nodeResourceManager = ControlCenter.getInstance().getNodeResourceManager();
        return nodeResourceManager == null ? null : nodeResourceManager.getResourceById(resId);
    }

    /**
     * 创建一个节点
     *
     * @param jobId 需要创建的作业ID
     * @param entry 创建的节点元素
     * @return 如果创建成功，那么返回值为true，否则返回值为false
     */
    @Override
    public boolean createNode(String jobId, Entry entry) {
        Job job = nodeOPService.getJobById(jobId);
        return job != null && job.addEntry(entry) && nodeOPService.updateJob(job);
    }

    /**
     * 删除某一个作业的节点
     *
     * @param jobId  需要操作的作业ID
     * @param nodeId 需要删除的节点ID
     * @return 如果删除成功，那么返回删除的节点Entry，否则返回值为false
     */
    @Override
    public boolean deleteNode(String jobId, String nodeId) {
        Job job = nodeOPService.getJobById(jobId);
        return job != null && job.removeEntry(nodeId) && nodeOPService.updateJob(job);
    }

    /**
     * 连接两个节点
     *
     * @param jobId    作业ID
     * @param sourceId 源节点
     * @param targetId 目标节点
     * @return 如果连接成功，那么返回值为true，否则返回值为false
     */
    @Override
    public boolean connect(String jobId, String sourceId, String targetId) throws FlowDescException {
        Job job = nodeOPService.getJobById(jobId);
        IEntry srcEntry = job.getEntry(sourceId);
        IEntry targetEntry = job.getEntry(targetId);
        boolean result;
        try {
            result = job.addConnector(srcEntry, targetEntry);
            System.err.println(EXmlParser.toXmlPretty(job));
        } catch (XmlParserException e) {
            return false;
        }
        nodeOPService.updateJob(job);
        return result;
    }

    /**
     * 移除两个节点之间的连线
     *
     * @param jobId       操作的作业ID
     * @param srcEntryId  源节点ID
     * @param destEntryId 目标节点ID
     * @return 如果连线移除成功，那么返回值为true，否则返回值为false
     */
    @Override
    public boolean deleteConnector(String jobId, String srcEntryId, String destEntryId) {
        Job job = nodeOPService.getJobById(jobId);
        boolean opResult = job.removeConnector(srcEntryId, destEntryId);
        if (opResult) {
            nodeOPService.updateJob(job);
        }
        return opResult;
    }

    /**
     * 根据作业ID检查作业的合法性
     *
     * @param jobId 需要检查合法性的作业ID
     * @return 如果检验合法，那么返回值为0，否则返回值为对应的错误代号
     */
    @Override
    public Integer validateGraphic(String jobId) {
        System.err.println("警告：检查作业合法性功能尚未真实实现");
        return 0;
    }

    /**
     * 更新作业节点的坐标
     *
     * @param jobId  节点所在的作业ID
     * @param nodeId 节点ID
     * @param toX    移动到的横坐标
     * @param toY    移动到的纵坐标
     * @return -
     */
    @Override
    public boolean moveNode(String jobId, String nodeId, String title, Integer toX, Integer toY) {
        Job job = nodeOPService.getJobById(jobId);
        IEntry movingNode = job.getEntry(nodeId);
        if (movingNode == null) {
            return false;
        }

        movingNode.setNodeName(title);
        movingNode.setX(toX);
        movingNode.setY(toY);

        return nodeOPService.updateJob(job);
    }

    /**
     * 测试运行作业
     *
     * @param jobId 需要运行的作业ID
     * @return 如果运行成功，那么返回值为true，否则返回值为false
     */
    @Override
    public boolean testRunningJob(String jobId) {
        Job job = nodeOPService.getJobById(jobId);
        if (job != null) {
            altassNode.run(new JDFWrapper(job));
            return true;
        }
        return false;
    }

    /**
     * 更新节点的配置信息
     *
     * @param jobId    需要配置的节点所在的作业ID
     * @param nodeId   节点ID
     * @param jsonData 需要配置的json数据
     * @return 如果节点配置成功，那么返回值为true，否则返回值为false
     */
    @Override
    public boolean configNode(String jobId, String nodeId, String jsonData) {
        Job job = nodeOPService.getJobById(jobId);
        if (job == null) {
            return false;
        }

        Entry configEntry = JSON.parseObject(jsonData, Entry.class);
        if (configEntry == null) {
            return false;
        }

        System.err.println("jsonData\n" + jsonData);

        IEntry entry = job.getEntry(nodeId);
        if (entry == null) {
            return false;
        }

        // 需要将配置的数据先转储到原有对象中
        configEntry.setNodeId(entry.getNodeId());
        configEntry.setX(entry.getX());
        configEntry.setY(entry.getY());
        configEntry.setType(entry.getType());
        configEntry.setInputParam(entry.getInputParam());
        configEntry.setOutputParam(entry.getOutputParam());
        configEntry.setInDegree(entry.getInDegree());
        configEntry.setOutDegree(entry.getOutDegree());
        configEntry.setNodeName(entry.getNodeName());
        configEntry.setNodeResource(entry.getNodeResource());
        configEntry.setState(entry.getState());
        configEntry.setNodeType(entry.getNodeType());
        configEntry.setDescriptionText(entry.getDescriptionText());
        configEntry.setExecutorClz(entry.getExecutorClz());

        configEntry.reset();

        Method[] methods = Entry.class.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("set")) {
                String opMethodName = methodName.substring(3, methodName.length());
                try {
                    // 调用新对象的对应get方法，如果get的对象不为null，那么需要设置到原来的对象中
                    Object data = Entry.class.getMethod("get" + opMethodName).invoke(configEntry);
                    // 忽略返回值
                    method.invoke(entry, data);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        nodeOPService.updateJob(job);
        return true;
    }

    /**
     * 获得节点的配置信息
     *
     * @param jobId  需要操作的作业ID
     * @param nodeId 需要操作的节点ID
     * @return 如果配置获取成功，那么返回值为节点配置信息，否则返回值为false
     */
    @Override
    public Entry getNodeConfiguration(String jobId, String nodeId) {
        Job job = nodeOPService.getJobById(jobId);
        return job == null ? null : (Entry) job.getEntry(nodeId);
    }

    /**
     * 修改节点间连线的类型
     *
     * @param jobId         作业ID
     * @param sourceId      源ID
     * @param targetId      目标ID
     * @param connectorType 连线类型
     * @return 如果连线类型修改成功，那么返回值为true，否则返回
     */
    @Override
    public boolean changeConnectorType(String jobId, String sourceId, String targetId, String connectorType) {
        Job job = nodeOPService.getJobById(jobId);
        List<Connector> connectors = job.getConnectors().getConnectors();
        Integer connectType = "success".equals(connectorType) ? Connector.CONNECT_TYPE_SUCCESS : Connector.CONNECT_TYPE_FAILURE;
        boolean changeResult = false;
        for (Connector connector : connectors) {
            if (connector.getSourceId().equals(sourceId) && connector.getTargetId().equals(targetId)) {
                connector.setConnectType(connectType);
                changeResult = true;
                break;
            }
        }

        return changeResult && nodeOPService.updateJob(job);
    }

}

