/*
 * Project: x-framework
 * Package Name: org.ike.etl.core.distribution.face
 * Author: Xuejia
 * Date Time: 2017/1/23 8:30
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.manager;


import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.executor.ExecuteContext;

/**
 * Class Name: IMissionOperator
 * Create Date: 2017/1/23 8:30
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 任务执行操作接口
 */
public interface IMissionOperator {
    /**
     * 唤醒一个线程
     *
     * @param entry 需要唤醒的元素
     */
    void wakeupMission(Entry entry);

    /**
     * 线性执行一个任务
     *
     * @param entry       需要线性执行的任务
     * @param lastContext 执行上下文
     */
    void executeMissionLinear(Entry entry, ExecuteContext lastContext);

    /**
     * 层级执行一个任务
     *
     * @param entry    需要层级执行的任务
     * @param pContext 执行上下文
     */
    void executeMissionLayer(Entry entry, ExecuteContext pContext);

    /**
     * 判断节点元素是否在阻塞态
     *
     * @param entry 判断元素是否处于阻塞状态
     * @return 如果元素处于阻塞状态，那么返回值为true，否则返回值为false
     */
    boolean isBlocking(Entry entry);

    /**
     * 释放阻塞状态的元素
     *
     * @param entry 需要释放阻塞状态的元素
     * @return 如果阻塞状态释放成功，那么返回值为true，否则返回值为false
     */
    boolean releaseBlocking(Entry entry);

    /**
     * 计算节点的阻塞影响alpha值
     *
     * @param entry 需要计算alpha值的节点
     * @return 返回节点的阻塞影响alpha值
     */
    int calculateBlockingAlpha(Entry entry);

    /**
     * 添加阻塞节点的阻塞值
     *
     * @param entry 需要添加阻塞值的节点
     * @return 返回当前节点的阻塞节点的alpha值
     */
    int increaseBlockingAlpha(Entry entry);

    /**
     * 移除一个元素
     *
     * @param entry 需要移除的元素
     * @return 返回移除后当前池中的执行总量
     */
    int removeEntry(Entry entry);


}
