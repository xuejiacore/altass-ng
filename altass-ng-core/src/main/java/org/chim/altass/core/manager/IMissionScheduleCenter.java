/*
 * Project: x-framework
 * Package Name: org.ike.etl.core.distribution.face
 * Author: Xuejia
 * Date Time: 2017/1/18 17:32
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.manager;


import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractExecutor;
import org.chim.altass.core.executor.ExecuteContext;
import org.chim.altass.core.executor.face.IExecutorListener;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.util.Map;

/**
 * Class Name: IMissionScheduleCenter
 * Create Date: 2017/1/18 17:32
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 任务调度中心接口
 * <p>
 * 根据需求不同实现分布式或者是单机运行的任务调度器
 */
public interface IMissionScheduleCenter {

    /**
     * 通知更新
     *
     * @param updateAnalysis 更新分析包
     */
    void notifyUpdate(UpdateAnalysis updateAnalysis);

    /**
     * 判断节点是否阻塞态
     *
     * @return 如果节点处于阻塞状态，那么返回值为true，否则返回值为false
     */
    Boolean isEntryBlocking(IEntry entry);

    /**
     * 释放阻塞节点的阻塞状态
     *
     * @param nodeId 需要释放的节点ID
     * @return 如果释放成功，那么返回值为true，否则返回值为false
     */
    Boolean releaseBlocking(String nodeId);

    /**
     * 运行时更新作业
     *
     * @param updatePkg 需要更新的作业差异包
     * @return 如果更新提交成功，那么返回值为true，否则返回值为false
     */
    Boolean updateRunningJob(UpdateAnalysis updatePkg);

    /**
     * 同层执行任务
     *
     * @param currExec    当前即将执行的任务节点
     * @param lastContext 上一个执行的任务的上下文信息
     * @return 返回当前执行的数目
     */
    int executeMissionLinear(AbstractExecutor currExec, ExecuteContext lastContext) throws ExecuteException;

    /**
     * 执行一个执行器任务
     *
     * @param currExec    需要执行的执行器任务
     * @param lastContext 上一个执行的任务的上下文信息
     * @return 返回当前作业池中的作业总数
     */
    int executeMissionAsChild(AbstractExecutor currExec, ExecuteContext lastContext) throws ExecuteException;

    /**
     * 执行一个Job类型执行器
     *
     * @param abstractExecutor 需要执行的Job执行器
     * @return 返回当前执行池中的执行总量
     */
    int executeMissionAsChild(AbstractExecutor abstractExecutor) throws ExecuteException;

    /**
     * 根据元素生成一个执行器并执行
     *
     * @param entry 需要生成的执行器
     * @return 返回当前的执行器总量
     * @throws ExecuteException 如果节点为空或者是节点未指定执行器，那么将抛出异常
     */
    int executeMissionAsChild(IEntry entry) throws ExecuteException;

    /**
     * 生成一个执行器
     *
     * @param entry 需要创建的执行器的构建元素
     * @return 返回一个执行器
     */
    AbstractExecutor generateExecutor(IEntry entry) throws ExecuteException;

    /**
     * 根据当前元素的描述（主要是对应的执行器描述）获得执行器实例
     * 根据元素的情况自动生成作业/节点执行器
     *
     * @param entry    需要执行的元素，可能是文件节点、FTP节点等
     * @param parentId 执行器的父ID（指的是作业号）
     * @return 返回可执行的执行器对象
     * @throws ExecuteException 如果执行节点为null或者是当前执行节点没有制定执行器，将会抛出执行异常
     */
    AbstractExecutor generateExecutor(IEntry entry, String parentId) throws ExecuteException;

    /**
     * 判断需要执行的执行器是否再执行器集合中
     *
     * @param abstractExecutor 需要添加判断的执行器
     * @return 如果存在，那么返回值为true，否则返回值为false
     */
    boolean executorIsExisted(AbstractExecutor abstractExecutor);

    /**
     * 获得前驱降值
     *
     * @return 返回前驱降值的数据
     */
    Map<String, Integer> getPreCountDownData();

    /**
     * 获得一个Entry对应的降值情况
     *
     * @param entryId 元素
     * @return 降值数值
     */
    int getPreCountDownCnt(String entryId);

    /**
     * 为某一个阻塞节点降因素值
     *
     * @param entryId 需要降值的节点的ID
     */
    void addPreCountDownCnt(String entryId);

    /**
     * 获得对应执行器的数目
     *
     * @param clazz 需要计数的执行器类
     * @return 返回指定执行器类的数目
     */
    int getExecutorSize(Class<? extends AbstractExecutor> clazz);

    /**
     * 从执行集合中移除一个执行器
     *
     * @param abstractExecutor 需要移除的执行器对象
     */
    void remove(AbstractExecutor abstractExecutor);

    /**
     * 根据执行器ID获得执行器
     *
     * @param executorId 执行器ID
     * @return 返回执行器ID对应的执行器实例
     */
    IEntry getExecutorById(String executorId);

    /**
     * 绑定执行器执行情况监听器
     *
     * @param listener 需要绑定的监听器
     */
    void bindingExecutingListener(IExecutorListener listener);
}
