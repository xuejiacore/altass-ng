/*
 * Project: x-framework
 * Package Name: org.ike.etl.core.distribution
 * Author: Xuejia
 * Date Time: 2017/1/18 17:58
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.executor;


import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.ExecuteException;

import java.lang.reflect.InvocationTargetException;

/**
 * Class Name: ExecutorHelper
 * Create Date: 2017/1/18 17:58
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@SuppressWarnings("Duplicates")
public class ExecutorHelper {
    /**
     * 根据当前元素的描述（主要是对应的执行器描述）获得执行器实例
     * 根据元素的情况自动生成作业/节点执行器
     *
     * @param entry    需要执行的元素，可能是文件节点、FTP节点等
     * @param parentId 执行器的父ID（指的是作业号）
     * @return 返回可执行的执行器对象
     * @throws ExecuteException 如果执行节点为null或者是当前执行节点没有制定执行器，将会抛出执行异常
     */
    public static AbstractExecutor generateExecutor(Entry entry, String parentId) throws ExecuteException {
        AbstractExecutor abstractExecutor = null;
        if (entry == null) {
            throw new ExecuteException("执行节点为空");
        } else {
            if (entry.getExecutorClz() == null) {
                throw new ExecuteException("未指定执行器");
            }
            try {
                abstractExecutor = entry.getExecutorClz().getConstructor(String.class).newInstance(entry.getNodeId());
                abstractExecutor.setParentExecuteId(parentId);
                abstractExecutor.setEntry(entry);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return abstractExecutor;
    }

    /**
     * 根据元素的情况生成作业/节点执行器
     *
     * @param entry 需要创建的执行元素
     * @return 返回可执行的执行器对象
     * @throws ExecuteException
     */
    public static AbstractExecutor generateExecutor(Entry entry) throws ExecuteException {
        return generateExecutor(entry, null);
    }
}
