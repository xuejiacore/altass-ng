/**
 * Project: x-framework
 * Package Name: org.ike.etl.assemble.core
 * Author: Xuejia
 * Date Time: 2016/12/21 21:07
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.service;


import org.chim.altass.core.configuration.NodeResource;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.FlowDescException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Name: INodePainterService
 * Create Date: 2016/12/21 21:07
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 节点绘制接口
 * <p>
 * 包含了节点绘制的常用操作：
 * 节点的创建、删除、编辑、修改
 * 节点间连线的创建、删除、编辑、修改
 */
public interface INodePainterService {

    /**
     * 初始化节点面板
     * 初始化节点面板的开始和结束节点
     *
     * @return 初始化的参数
     */
    HashMap<String, Object> initPainter();

    /**
     * 获得节点资源
     *
     * @return 节点资源信息
     */
    Map<String, List<NodeResource>> obtainNodeResource();

    /**
     * 获得节点资源
     *
     * @return 节点资源信息
     */
    List<NodeResource> obtainResources();

    /**
     * 根据资源ID获得资源配置数据
     *
     * @param resId 资源ID
     * @return 资源的配置数据
     */
    NodeResource obtainResource(String resId);

    /**
     * 创建节点
     * 创建一个节点的时候需要初始化节点信息，返回当前节点的数据
     *
     * @param jobId 需要创建的作业ID
     * @param entry 创建的节点元素
     * @return 如果节点创建成功，那么返回值为true，否则返回值为false
     */
    boolean createNode(String jobId, Entry entry);

    /**
     * 删除节点
     * 删除节点的时候需要返回当前节点的
     *
     * @param jobId  作业ID
     * @param nodeId 需要删除的节点ID
     * @return 如果节点删除成功，那么返回值为true，否则返回值为false
     */
    boolean deleteNode(String jobId, String nodeId);

    /**
     * 建立节点之间的连线
     *
     * @param jobId    作业ID
     * @param sourceId 源节点
     * @param targetId 目标节点
     * @return 如果连接成功，那么返回值为true，否则返回值为false
     */
    boolean connect(String jobId, String sourceId, String targetId) throws FlowDescException;

    /**
     * 删除一个节点连线
     *
     * @param jobId       操作的作业ID
     * @param srcEntryId  源节点ID
     * @param destEntryId 目标节点ID
     * @return 如果连线移除成功，那么返回值为true，否则返回值为false
     */
    boolean deleteConnector(String jobId, String srcEntryId, String destEntryId);

    /**
     * 检查作业的执行节点图是否合法
     *
     * @param jobId 需要检查合法性的作业ID
     * @return 如果合法，那么返回值为0，不合法返回对应的错误代码
     */
    Integer validateGraphic(String jobId);

    /**
     * 移动节点，更新节点的坐标信息
     *
     * @param jobId  节点所在的作业ID
     * @param nodeId 节点ID
     * @param title  节点的名称
     * @param toX    移动到的横坐标
     * @param toY    移动到的纵坐标
     * @return 如果移动更新成功，那么返回值为true，否则返回值为false
     */
    boolean moveNode(String jobId, String nodeId, String title, Integer toX, Integer toY);

    /**
     * 测试运行作业
     *
     * @param jobId 需要运行的作业ID
     * @return 如果作业运行成功，那么返回值为true，否则返回值为false
     */
    boolean testRunningJob(String jobId);

    /**
     * 配置一个作业节点的配置信息
     *
     * @param jobId    需要配置的节点所在的作业ID
     * @param nodeId   节点ID
     * @param jsonData 需要配置的json数据
     * @return 如果配置成功，那么返回值为true，否则返回值为false
     */
    boolean configNode(String jobId, String nodeId, String jsonData);

    /**
     * 获得节点的配置信息
     *
     * @param jobId  需要操作的作业ID
     * @param nodeId 需要操作的节点ID
     * @return 返回获得的节点配置信息
     */
    Entry getNodeConfiguration(String jobId, String nodeId);

    /**
     * 修改连线的类型
     *
     * @param jobId         作业ID
     * @param sourceId      源ID
     * @param targetId      目标ID
     * @param connectorType 连线类型
     * @return 如果修改成功，那么返回值为true，否则返回值为false
     */
    boolean changeConnectorType(String jobId, String sourceId, String targetId, String connectorType);
}
