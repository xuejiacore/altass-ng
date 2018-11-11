/*
 * Project: x-framework
 * Package Name: org.ike.etl.core.distribution.dist.impl
 * Author: Xuejia
 * Date Time: 2017/1/19 14:26
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.manager;

import org.chim.altass.core.executor.face.IExecutorListener;
import org.chim.altass.base.utils.type.StringUtil;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractExecutor;
import org.chim.altass.core.executor.ExecuteContext;
import org.chim.altass.toolkit.job.UpdateAnalysis;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;
import static org.chim.altass.core.constant.ExecutorAttr.*;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class Name: DistMissionScheduleCenter
 * Create Date: 2017/1/19 14:26
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 分布式任务调度器
 */
@Deprecated
@SuppressWarnings("Duplicates")
@Component(value = "distMissionScheduleCenter")
public class DistMissionScheduleCenter implements IMissionScheduleCenter {

    private static final String K_REDISSON_EXECUTOR_MAP = "_EUREKA_REDISSON_EXECUTOR_MAP";

    private IExecutorListener executorListener = null;

    private RMap<String, AbstractExecutor> executorMap = null;

    private String redisHost = null;

    private Integer redisPort = null;

    private RedissonClient redissonClient = null;

    public DistMissionScheduleCenter() {
        System.err.println("使用分布式作业调度池");
    }

    @Override
    public void notifyUpdate(UpdateAnalysis updateAnalysis) {

    }

    @Override
    public Boolean isEntryBlocking(IEntry entry) {
        return null;
    }

    @Override
    public Boolean releaseBlocking(String nodeId) {
        return null;
    }

    @Override
    public Boolean updateRunningJob(UpdateAnalysis updatePkg) {
        return false;
    }

    @Override
    public int executeMissionLinear(AbstractExecutor currExec, ExecuteContext lastContext) throws ExecuteException {
        return 0;
    }

    @Override
    public int executeMissionAsChild(AbstractExecutor currExec, ExecuteContext lastContext) throws ExecuteException {
        if (currExec.validate()) {
            // 跟踪当前执行器的父执行器相关属性，如果父执行器为空，那么取当前执行器的同级父执行器
            // 如果lastContext为null，那么说明是第一个节点，也就是job的运行开始，设置这个开始的节点对应的父节点的ID就是当前节点
            String parentExecuteId = lastContext == null ? currExec.getExecuteId() : (String) lastContext.getAttribute(ATTR_EXECUTE_PARENT_ID);
            if (StringUtil.isNotEmpty(parentExecuteId)) {
                currExec.setParentExecuteId(parentExecuteId);                                            // 设置执行器的父ID

                // TODO：检测到有任何的节点没有执行，都要进行处理，处理的原则是每次启动下一个节点线程的时候，都要将当前节点的上下文
                // TODO：引用添加到下一个节点的上下文对象的上一个节点信息中，以便能够进行回溯
                currExec.getContext().addTopContext(parentExecuteId, lastContext);
            }
            Integer nodeType = currExec.getEntry().getNodeType();
            String threadName = "EUREKA-" + (nodeType == null ? "TYPE_UNDEFINED" : nodeType) + "-" + currExec.getExecuteId();
            Thread thread = new Thread(currExec, threadName);
            thread.start();

            executorMap.put(currExec.getExecuteId(), currExec);
            currExec.getContext().addAttribute(ATTR_THREAD_ID, thread.getId());
        }
        return executorMap.size();
    }

    @Override
    public int executeMissionAsChild(AbstractExecutor abstractExecutor) throws ExecuteException {
        return executeMissionAsChild(abstractExecutor, null);
    }

    @Override
    public int executeMissionAsChild(IEntry entry) throws ExecuteException {
        return executeMissionAsChild(generateExecutor(entry));
    }

    @Override
    public AbstractExecutor generateExecutor(IEntry entry) throws ExecuteException {
        return generateExecutor(entry, null);
    }

    @Override
    public AbstractExecutor generateExecutor(IEntry entry, String parentId) throws ExecuteException {
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
                abstractExecutor.bindingExecListener(executorListener);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return abstractExecutor;
    }

    @Override
    public boolean executorIsExisted(AbstractExecutor abstractExecutor) {
        return false;
    }

    @Override
    public ConcurrentHashMap<String, Integer> getPreCountDownData() {
        return null;
    }

    @Override
    public int getPreCountDownCnt(String entryId) {
        return 0;
    }

    @Override
    public void addPreCountDownCnt(String entryId) {

    }

    @Override
    public int getExecutorSize(Class<? extends AbstractExecutor> clazz) {
        return 0;
    }

    @Override
    public void remove(AbstractExecutor abstractExecutor) {

    }

    @Override
    public IEntry getExecutorById(String executorId) {
        return null;
    }

    @Override
    public void bindingExecutingListener(IExecutorListener listener) {
        this.executorListener = listener;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
        if (this.redisPort != null && redissonClient == null) {
            Config config = new Config();
            config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
            this.redissonClient = Redisson.create(config);
            this.executorMap = this.redissonClient.getMap(K_REDISSON_EXECUTOR_MAP);
        }
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
        if (this.redisHost != null && redissonClient == null) {
            Config config = new Config();
            config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
            this.redissonClient = Redisson.create(config);
            this.executorMap = this.redissonClient.getMap(K_REDISSON_EXECUTOR_MAP);
        }
    }
}
