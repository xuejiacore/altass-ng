/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.domain
 * Author: Xuejia
 * Date Time: 2016/12/19 18:46
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain;


import org.chim.altass.core.configuration.NodeResource;
import org.chim.altass.core.domain.connector.Connector;
import org.chim.altass.core.domain.meta.InputParam;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.domain.meta.OutputParam;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractExecutor;
import org.chim.altass.core.executor.ExecuteContext;
import org.redisson.api.RCountDownLatch;

import java.util.List;

/**
 * Class Name: IEntry
 * Create Date: 2016/12/19 18:46
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface IEntry {

    /**
     * 获得当前节点的id
     *
     * @return 当前节点的id
     */
    String getNodeId();

    /**
     * 设置节点id
     *
     * @param nodeId 节点id
     */
    void setNodeId(String nodeId);

    /**
     * 获得节点的类型
     *
     * @return 节点的类型
     */
    Integer getNodeType();

    /**
     * 设置节点的名称
     *
     * @param nodeName 节点名称
     */
    void setNodeName(String nodeName);

    /**
     * 节点所在的横坐标，用于ui处理
     *
     * @param x 横坐标
     */
    void setX(Integer x);

    /**
     * 节点所在的纵坐标，用于ui处理
     *
     * @param y 纵坐标
     */
    void setY(Integer y);

    /**
     * 获得x坐标
     *
     * @return x坐标
     */
    Integer getX();

    /**
     * 获得y坐标
     *
     * @return y坐标
     */
    Integer getY();

    /**
     * 获得节点的类型
     *
     * @return 节点类型
     */
    Integer getType();

    /**
     * 获得节点名称
     *
     * @return 节点名称
     */
    String getNodeName();

    /**
     * 获得节点的资源描述
     *
     * @return 节点的资源描述
     */
    NodeResource getNodeResource();

    /**
     * 获得节点的状态
     *
     * @return 节点状态
     */
    Integer getState();

    /**
     * 获得节点的描述文本
     *
     * @return 节点描述
     */
    String getDescriptionText();

    /**
     * 获得当前节点执行器对应的class名称
     *
     * @return 当前节点执行器对应的class名称
     */
    String getExecutorClzName();

    /**
     * 获得节点执行器的类型
     *
     * @return 节点执行器的类型
     */
    Class<? extends AbstractExecutor> getExecutorClz();

    /**
     * 获得节点的所有入度
     *
     * @return 当前节点的所有入度连线
     */
    List<Connector> getInDegree();

    /**
     * 获得节点的所有出度
     *
     * @return 当前节点的所有出度连线
     */
    List<Connector> getOutDegree();

    /**
     * 设置节点的执行状态
     *
     * @param state 节点的执行状态
     */
    void setState(Integer state);

    /**
     * 尝试暂停当前运行中的节点
     *
     * @param recursion 是否递归暂停所有子作业
     * @return 如果暂停成功，那么返回值为true，否则返回值为false
     */
    boolean pause(boolean recursion);

    /**
     * 尝试暂停并且监听暂停的事件(同步方法)
     *
     * @return 如果暂停成功，那么返回值为true，否则返回值为false
     */
    boolean pauseSync();

    /**
     * 尝试从暂停的状态中恢复运行
     *
     * @return 如果状态设置成功，那么返回值为true，否则返回值为false
     */
    boolean resume();

    /**
     * 尝试停止当前节点
     *
     * @return 如果停止成功，那么返回值为true，否则返回值为false
     */
    boolean stop() throws ExecuteException;

    /**
     * 为当前节点添加输出参数
     *
     * @param param 需要添加的输出参数
     */
    void addOutputParameter(MetaData param);

    /**
     * 获得当前节点的所有直接前驱节点
     *
     * @return 当前节点直接前驱节点
     */
    List<String> obtainPrecursors();

    /**
     * 获得当前节点的所有直接后继节点
     *
     * @return 当前节点的直接后继节点
     */
    List<String> obtainSuccessors();

    /**
     * 获得当前节点的输入参数
     *
     * @return 当前节点的输入参数
     */
    InputParam getInputParam();

    /**
     * 获得当前节点的输出参数
     *
     * @return 当前节点的输出参数
     */
    OutputParam getOutputParam();

    /**
     * 立即热更新重载运行中的作业
     * <p>
     * 在作业运行状态下调整作业的各个配置，在热更新的时候，不允许变更参数，只能够以整个任务新增或删除，或者是调整路线的形式
     *
     * @return 如果作业更新成功，那么返回值为true，否则返回值为false
     */
    boolean immediateReloading();

    /**
     * 为当前的节点添加出度
     *
     * @param connector 出度连线
     * @return 如果添加成功，那么返回值为true，否则返回值为false
     */
    boolean addOutDegree(Connector connector);

    /**
     * 删除当前节点的某一个出度
     *
     * @param connector 出度线
     * @return 如果删出成功，那么返回值为true，否则返回值为false
     */
    boolean delOutDegree(Connector connector);

    /**
     * 为当前的节点添加入度
     *
     * @param connector 入度连线
     * @return 如果添加成功，那么返回值为true，否则返回值为false
     */
    boolean addInDegree(Connector connector);

    /**
     * 删除当前节点的某一个入度
     *
     * @param connector 入度线
     * @return 如果删除成功，那么返回值为true，否则返回值为false
     */
    boolean delInDegree(Connector connector);


    /**
     * 唤醒节点
     *
     * @param context 节点上下文
     * @return 如果唤醒成功，那么返回值为true，否则返回值为false
     */
    boolean wakeup(ExecuteContext context);

    /**
     * 获得count down latch
     *
     * @return 分布式count down latch
     */
    RCountDownLatch obtainGlobalLatch();

    /**
     * 获得执行上下文
     *
     * @return 执行器上下文
     */
    ExecuteContext obtainContext();

    /**
     * 唤醒当前元素的执行器
     */
    void wakeup();

    /**
     * 优雅停止节点
     */
    void exitGracefully();
}
