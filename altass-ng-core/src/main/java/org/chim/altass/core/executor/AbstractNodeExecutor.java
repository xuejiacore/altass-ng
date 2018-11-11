/*
 * Project: x-framework
 * Package Name: org.ike.etl.core.executor
 * Author: Xuejia
 * Date Time: 2016/12/19 11:23
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.chim.altass.core.annotation.RuntimeAutowired;
import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.core.constant.ExecuteStatus;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.Node;
import org.chim.altass.core.domain.buildin.attr.ACommon;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.domain.meta.InputParam;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.domain.meta.OutputParam;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.face.INodeEvent;
import org.chim.altass.core.executor.face.INodeLog;
import org.chim.altass.core.executor.general.JobExecutor;
import org.chim.altass.core.ext.ContextMap;
import org.chim.altass.toolkit.JDFWrapper;
import org.redisson.api.RLock;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.chim.altass.core.constant.ExecutorAttr.ATTR_NEXT_ENTRY_INFO;
import static org.chim.altass.core.constant.ExecutorAttr.ATTR_NEXT_ENTRY_SIZE;

/**
 * Class Name: AbstractNodeExecutor
 * Create Date: 2016/12/19 11:23
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractNodeExecutor extends AbstractExecutor implements INodeEvent, INodeLog {

    protected Logger logger = LogManager.getLogger(super.getClass());

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    protected AbstractNodeExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }


    @Override
    public void onException(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onSkip(String reason) {

    }

    /**
     * To process executor inner parameters with entry default data source.
     * ACommon could be parse to current.
     *
     * @return true will be return if process successful, otherwise false will be return
     * @throws ExecuteException process exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public final boolean beforeInit() throws ExecuteException {
        // Obtain the executor type that will be execute later soon.
        Class<? extends AbstractExecutor> executorClz = this.entry.getExecutorClz();

        if (executorClz != null) {
            Field[] declaredFields = executorClz.getDeclaredFields();
            try {

                InputParam inputParam = this.context.getInputParam();

                List<MetaData> params = new ArrayList<>();
                ContextMap fluxContext = this.context.getFluxContext();
                if (fluxContext != null) {
                    for (ExecuteContext executeContext : fluxContext.values()) {
                        OutputParam outputParam = executeContext.getOutputParam();
                        if (outputParam == null) continue;
                        List<MetaData> p = outputParam.getParams();
                        if (p != null)
                            params.addAll(p);
                    }
                }
                Map<String, Object> paramMap = new HashMap<>();
                if (inputParam != null) {
                    params.addAll(inputParam.getParams());
                }
                for (MetaData metaData : params) {
                    paramMap.put(metaData.getField(), metaData.getValue());
                }

                // extend parse context
                if (((Entry) this.entry).getCommon() != null)
                    ((Entry) this.entry).getCommon().extParseContext(paramMap);

                for (Field field : declaredFields) {
                    // recognize whether is Autowire field
                    RuntimeAutowired autowire = field.getDeclaredAnnotation(RuntimeAutowired.class);
                    Class<?> fieldType = field.getType();
                    if (autowire != null) {
                        Object dataSource = ((Entry) this.entry).getCommon();
                        boolean paramParse = autowire.analyzable();

                        // obtain custom data source
                        Class source = autowire.source();
                        if (!ACommon.class.equals(source)) {

                            // Not default ACommon type data source. Replace the character 'A' prefix.
                            String propertyName = source.getSimpleName().replaceAll("^A", "");
                            String dataSourceGetMethodName = "get" + propertyName.substring(0, 1).toUpperCase() +
                                    propertyName.substring(1);

                            Method getMethod = Entry.class.getMethod(dataSourceGetMethodName, (Class<?>) null);
                            dataSource = getMethod.invoke(this.entry, (Object) null);
                        }

                        String parameterSimpleName = fieldType.getSimpleName();
                        if (dataSource instanceof ACommon) {
                            // Had been sure that the type of data source is the default type ACommon.
                            String keyExpr = parameterSimpleName.substring(0, 1).toLowerCase() + parameterSimpleName.substring(1);
                            Object attr = ((ACommon) dataSource).getAttr(keyExpr, paramParse);

                            if (attr instanceof JSONObject) {

                                Object data = JSON.parseObject(((JSONObject) attr).toJSONString(), fieldType);
                                boolean isPrivate = Modifier.isPrivate(fieldType.getModifiers());

                                field.setAccessible(true);
                                field.set(this, data);
                                if (isPrivate) {
                                    field.setAccessible(false);
                                }
//                                Method setMethod = executorClz.getMethod("set" + parameterSimpleName, fieldType);
                                // Obtain data from entry default data source and set to executor's member.
//                                setMethod.invoke(this, data);
                            }

                        }
                    }
                }
            } catch (Exception e) {
                throw new ExecuteException(e);
            }
        } else {
            throw new ExecuteException("Executor class could not be null");
        }
        return this.beforeNodeInit();
    }

    public boolean beforeNodeInit() {
        return true;
    }

    /**
     * 初始化节点信息
     *
     * @return 如果初始化操作成功，返回值为true，否则返回值为false，节点异常
     * @throws ExecuteException -
     */
    @Override
    public boolean onInit() throws ExecuteException {
        /*
         * 初始化节点，获取节点信息
         */
        return true;
    }

    @Override
    public boolean hadStopManual() throws ExecuteException {
        return false;
    }

    @Override
    public boolean clearStopTag() throws ExecuteException {
        return false;
    }

    @Override
    public boolean onStart() throws ExecuteException {
        /*
         * 节点操作开始
         */
        return onNodeStart();
    }

    @Override
    public boolean beforeProcess() throws ExecuteException {
        List<String> successors;
        if ((successors = this.entry.obtainSuccessors()) != null) {
            Job job = getCurrentJob();
            List<IEntry> nextNodes = job.getEntries(successors);

            if (nextNodes == null || nextNodes.size() == 0) {
                if (!validate(nextNodes, job.getEndNode())) {
                    throw new ExecuteException("不合法的开始节点");
                }
                throw new ExecuteException("开始节点没有后继节点");
            }
            // 设置下一个节点的数量信息
            StringBuilder nextNodeInfo = new StringBuilder("[");
            for (IEntry nextNode : nextNodes) {
                nextNodeInfo.append(nextNode.getNodeType()).append(":").append(nextNode.getNodeId()).append(", ");
            }
            context.addAttribute(ATTR_NEXT_ENTRY_SIZE, nextNodes.size());
            context.addAttribute(ATTR_NEXT_ENTRY_INFO, nextNodeInfo.substring(0, nextNodeInfo.length() - 2) + "]");
        }
        logger.trace("-->> Context:\n" + context + "\n");
        return true;
    }

    @Override
    public final boolean processing() throws ExecuteException {
        try {
            if (onNodeProcessing()) {
                currentNodeExecuteStatus = ExecuteStatus.SUCCESS;
            } else {
                currentNodeExecuteStatus = ExecuteStatus.FAILURE;
                EXECUTOR_LOGGER("msg", "OnNodeProcessing Failure, returned false.");
                return false;
            }
        } catch (Exception e) {
            onNodeFailure();
            failureLog();
            currentNodeExecuteStatus = ExecuteStatus.FAILURE;
            throw new ExecuteException(e);
        }
        // 此处不管执行成功还是执行失败都要返回true，让节点继续进行下去
        return true;
    }

    @Override
    public void afterProcess() throws ExecuteException {
        ExecuteStatus status = obtainExecuteStatus();
        if (status.equals(ExecuteStatus.SUCCESS)) {
            // 当前节点执行器执行成功
            this.onNodeSuccess();
            this.successLog();
        } else if (status.equals(ExecuteStatus.FAILURE)) {
            // 当前节点执行器执行失败
            this.onNodeFailure();
            this.failureLog();
        }
    }

    /**
     * 验证开始节点之后是否是直接的结束节点
     *
     * @param nextNodes 下一节点集合
     * @param endNode   结束节点
     * @return 如果满足作业节点要求，那么返回值为true，否则返回值为false
     */
    private boolean validate(List<IEntry> nextNodes, Node endNode) {
        // TODO：此处废弃
        for (IEntry nextNode : nextNodes) {
            if (((Entry) nextNode).getNodeId().equals(endNode.getNodeId())) {
                return false;
            }
        }
        return endNode.obtainPrecursors().size() >= 1;
    }

    @Override
    public void onFinally() throws ExecuteException {
        Job job = getCurrentJob();

        List<String> nextEntriesWillExecute;                                // 下一批即将执行的节点
        List<IEntry> nextEntriesWillSkippedByExecStatus = null;              // 由于当前节点的执行状态影响下一批不执行节点的集合

        /*
         * 判断节点是否有失败线 TODO：需要改造，包含其它的线类型
         * 如果有失败线并且当前节点执行失败，成功线对应的分支不走，反之毅然
         * 对于执行的线，对当前节点之后的节点进行动态因素调节
         */
        // 判断是否有失败线
        boolean hasFailureConnection = ((Entry) this.entry).hasSuccessorsByType(STATUS_FAILURE);
        if (hasFailureConnection) {

            nextEntriesWillExecute = ((Entry) this.entry).obtainSuccessorsByType(currentNodeExecuteStatus.value(), false);
            // 获得下一个不执行节点：成功线对应节点不执行，使用反转参数获取节点集合
            nextEntriesWillSkippedByExecStatus = job.getEntries(((Entry) this.entry).obtainSuccessorsByType(currentNodeExecuteStatus.value(), true));
        } else {
            // 没有失败线
            nextEntriesWillExecute = this.entry.obtainSuccessors();
        }

        // 发现下一个执行节点
        logger.trace("是否有失败线:" + hasFailureConnection + " 获得下一个 " + currentNodeExecuteStatus.info() +
                " 的执行节点集合：" + nextEntriesWillExecute + "，不执行节点集合：" + nextEntriesWillSkippedByExecStatus);

        // 如果下一个跳过节点为null（没有后继节点），则不对阻塞因素进行调节
        if (nextEntriesWillSkippedByExecStatus != null) {
            for (IEntry skippedSuccessorsEntry : nextEntriesWillSkippedByExecStatus) {

                // 获得需要调节运行因素的节点集合
                List<IEntry> entriesNeedToAdjustAlpha = ((Entry) skippedSuccessorsEntry).getNextEntry(job, skippedSuccessorsEntry,
                        AbstractBlockingExecutor.class.getName(), true);

                for (IEntry entryNeedToAdjust : entriesNeedToAdjustAlpha) {
                    IEntry blockingExecutor = missionScheduleCenter.getExecutorById(entryNeedToAdjust.getNodeId());

                    if (blockingExecutor == null) {
                        // 如果从作业池中不能获取指定节点的执行器实例，那么需要进行预调节
                        missionScheduleCenter.addPreCountDownCnt(entryNeedToAdjust.getNodeId());
                    } else {
                        // 由于不走分支进行阻塞因素动差调节的，不需要设置上下文信息，传入的参数为null
                        blockingExecutor.wakeup();
                    }
                }
            }
        }

        // 下一执行节点集合不为null，那么开始准备节点的执行队列
        if (nextEntriesWillExecute != null) {

            List<IEntry> nextNodes = job.getEntries(nextEntriesWillExecute);
            if (nextNodes == null || nextNodes.size() == 0) {
                if (!validate(nextNodes, job.getEndNode())) {
                    logger.error("不合法的开始节点");
                    throw new ExecuteException("不合法的开始节点");
                }
                logger.error("开始节点没有后继节点");
                throw new ExecuteException("开始节点没有后继节点");
            }

            //  遍历所有的下一个需要执行的节点
            for (IEntry nextNode : nextNodes) {

                // 如果发现下一个执行节点是一个嵌套的子作业，需要获取这个子作业的下一个执行节点，如果有，那么也一起加入到
                // 当前的作业执行器中，作为下一个作业节点执行
                // TODO:此处需要考虑到子作业节点是否是需要阻塞的，如果是阻塞的，
                if (nextNode.getExecutorClz().isAssignableFrom(JobExecutor.class)) {
                    if (!((Job) nextNode).getIsObstructive()) {
                        // 获取子作业的后继节点，将这些后继节点添加到当前节点的后继（也就是忽略子作业节点，子作业节点只是形式存在）
                        List<String> childJobSuccessorIds = nextNode.obtainSuccessors();
                        List<IEntry> childJobNextNodes = job.getEntries(childJobSuccessorIds);
                        for (IEntry jobNextNode : childJobNextNodes) {
                            Integer nodeType = jobNextNode.getNodeType();
                            logger.trace("子作业中心作业池添加作业节点信息：" + (nodeType == null ? "TYPE_UNDEFINED" : nodeType) + "_" + jobNextNode.getNodeId());
                            logger.trace("当前准备队列：" + addToExecutingQueue(getJobId(), jobNextNode, this));
                        }
                    } else {
                        // 子作业是阻塞同步类型的，那么需要讲子作业的后继节点添加到子作业的End的后继节点
                        List<String> childJobSuccessorIds = nextNode.obtainSuccessors();
                        List<IEntry> jobSuccessors = job.getEntries(childJobSuccessorIds);
                        ((Job) nextNode).setSuccessors(getJobId(), jobSuccessors);
                    }
                }
                // 作业中心作业池添加作业节点信息
                Integer nodeType = nextNode.getNodeType();
                logger.trace("作业中心作业池添加作业节点信息：" + (nodeType == null ? "TYPE_UNDEFINED" : nodeType) + "_" + nextNode.getNodeId());
                logger.trace("当前准备队列：" + addToExecutingQueue(getJobId(), nextNode, this));
                // TODO：作业监控中心启动对即将运行节点的监控
            }
        }

    }

    /**
     * 作业节点完成执行后，获得下一个即将执行的执行队列，
     */
    @Override
    public void finished() throws ExecuteException {
        logger.trace("节点执行处理完成");
        IEntry entry;
        if (willExecExecutor != null) {
            /*
             * 注意：
             *
             * 在生成执行器的时候（特别针对阻塞型），为了同步一个阻塞型执行节点的解锁次数，必须为当前节点即将准备进行
             * 下一个可能的阻塞执行器时，加同步锁
             *
             * 此同步与org.ike.eurekacore.core.executor.AbstractNodeExecutor.finished 中的同步保持数据一致性
             *
             * 2017-01-17 18:00:14
             * -- By Xuejia
             */
            RLock lock = distToolkit.getLock(EurekaSystemRedisKey.EUREKA$LOCKER_WILL_EXE, 0);

            lock.lock();
            while ((entry = willExecExecutor.poll()) != null) {
                // 不允许生成并执行执行器的时候对阻塞节点进行降值操作
                // 1、此处根据节点的情况创建开始节点以下的其它节点情况并执行
                // TODO: 下一个节点是一个作业，按照作业的方式运行
                if (entry.getExecutorClz().isAssignableFrom(JobExecutor.class)) {
                    rpcService.runAsLinear(new JDFWrapper(entry), this.parentExecuteId,
                            this.executeId);
                    continue;
                }
                rpcService.runAsLinear(new JDFWrapper(entry), this.parentExecuteId,
                        this.executeId);
            }
            lock.unlock();
        }
    }

    @Override
    protected boolean onDisconnect() {
        System.err.println("断开当前节点的直接前驱后继");
        return true;
    }

    /**
     * 获得当前节点所在的作业ID
     * [节点特有方法]
     *
     * @return 作业ID
     */
    public String getJobId() {
        return getParentExecuteId();
    }

    @Override
    public void successLog() {
        logger.trace("节点执行成功日志");
    }

    @Override
    public void failureLog() {
        logger.error("节点执行失败日志");
    }

    @Override
    public void exceptionLog() {
        logger.trace("节点执行异常日志");
    }

    @Override
    public boolean onNodeStart() {
        return true;
    }

    @Override
    public boolean onNodeSuccess() {
        return false;
    }

    @Override
    public boolean onNodeFailure() {
        return true;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
