/*
 * Project: x-framework
 * Package Name: org.ike.etl.core.distribution.impl
 * Author: Xuejia
 * Date Time: 2017/1/18 17:42
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.manager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.chim.altass.core.executor.face.IExecutorListener;
import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.EXmlParser;
import org.chim.altass.base.utils.type.StringUtil;
import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.core.constant.Event;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.connector.Connector;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractBlockingExecutor;
import org.chim.altass.core.executor.AbstractExecutor;
import org.chim.altass.core.executor.AbstractJobExecutor;
import org.chim.altass.core.executor.ExecuteContext;
import org.chim.altass.core.executor.general.EndExecutor;
import org.chim.altass.core.ext.ContextCacheMap;
import org.chim.altass.core.ext.ContextMap;
import org.chim.altass.core.ext.ExecEntryMap;
import org.chim.altass.toolkit.RedissonToolkit;
import org.chim.altass.toolkit.job.UpdateAnalysis;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;
import static org.chim.altass.core.constant.ExecutorAttr.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Class Name: SAMissionScheduleCenter
 * Create Date: 2017/1/18 17:42
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Stand-Alone Mission Schedule Center
 * 任务调度中心
 * 2018-01-08 后当前任务中心已经升级改造为支持分布式的任务调度中心
 */
@SuppressWarnings({"Duplicates", "unchecked"})
@Component("missionScheduleCenter")
public class MissionScheduleCenter implements IMissionScheduleCenter {

    protected static Logger logger = LogManager.getLogger(IMissionScheduleCenter.class);

    /**
     * 执行器监听器
     * 用于回调当前执行器的执行ID以及当前的状态
     */
    private static IExecutorListener executorListener = null;

    /**
     * 执行器集合
     */
    private static ExecEntryMap executorMap = null;

    /**
     * 阻塞等待节点
     * K：阻塞的节点ID，V：是否阻塞（当前只要在该Map中的ID都会作为阻塞节点判定）
     */
    private static Map<String, Boolean> blockMap = null;

    /**
     * 进行预降值的预存信息，该预存信息用于处理节点走互斥线导致运行时CountDownLatch的值不同，更重要的是，该数据被用于当
     * 阻塞节点没有创建的时候，用于创建时的阻塞因素的初始化
     */
    private static Map<String, Integer> preCountDownData = null;

    /**
     * 作业节点上下文信息
     */
    private static ContextCacheMap contextCache;

    private static final RedissonToolkit distToolkit;

    static {
        // 分布式能力支持工具箱
        distToolkit = RedissonToolkit.getInstance();
        // 全局 preCountDown 映射池
        preCountDownData = distToolkit.getMap(EurekaSystemRedisKey.EUREKA$GLOBAL_PRE_CTD, 0);
        // 全局阻塞池
        blockMap = distToolkit.getMap(EurekaSystemRedisKey.EUREKA$GLOBAL_EXES_BLKS, 0);
        // 执行器池
        executorMap = new ExecEntryMap();
        // 全局上下文缓存池
        contextCache = new ContextCacheMap();
    }

    @Override
    public void notifyUpdate(UpdateAnalysis updateAnalysis) {
        String updateJobId = updateAnalysis.getJobId();
        String redisKey = Event.PUBLISH_JOB_REARRANGEMENT.redisKey() + "#" + updateJobId;
        try {
            String xml = EXmlParser.toXml(updateAnalysis);
            distToolkit.publish(redisKey, xml);
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断当前元素是否阻塞
     *
     * @param entry 需要判断的元素实例
     * @return 如果元素阻塞状态，那么返回值为true，否则返回值为false
     */
    @Override
    public Boolean isEntryBlocking(IEntry entry) {
        Boolean isBlocking;
        RLock lock = distToolkit.getLock(EurekaSystemRedisKey.EUREKA$LOCKER_BLK_MAP, 0);
        lock.lock();
        // 此处同步锁控制防止判定错误
        isBlocking = blockMap.get(entry.getNodeId()) != null;
        if (!isBlocking) {
            blockMap.put(entry.getNodeId(), true);
        }
        lock.unlock();
        return isBlocking;
    }

    /**
     * 释放阻塞
     *
     * @param nodeId 需要释放的节点ID
     * @return 如果阻塞释放成功，那么返回值为true，否则返回值为false
     */
    @Override
    public Boolean releaseBlocking(String nodeId) {
        return blockMap.remove(nodeId);
    }

    /**
     * 运行时更新作业差异包
     *
     * @param updatePkg 需要更新的作业差异包
     * @return 如果差异包更新成功，那么返回值为true，否则返回值为false
     */
    @Override
    public Boolean updateRunningJob(UpdateAnalysis updatePkg) {
        String jobId = updatePkg.getJobId();
        IEntry jobEntry = getExecutorById(jobId);
        if (jobEntry == null || jobEntry.getExecutorClz().isAssignableFrom(AbstractJobExecutor.class)) {
            return false;
        }

        Job job = (Job) jobEntry;
        // TODO:1、暂停当前更新作业的所有任务（子作业除外）
        boolean pauseResult = job.pause(false);
        if (pauseResult) {
            // TODO：2、暂停完成所有的任务后，从更新包中获得所有需要更新的内容
            // 从作业任务池中获取作业
            // 1. 遍历所有新增的节点和连线，挂载新节点到作业任务中，
            // 1.1 如果挂载的前驱后继已经启动了，那么此时要建立起前驱后继关系，建立上下文关联，发现前驱后继的任务类型，建立起新的数据流
            //
            // 1.2 如果挂载的前驱后继均为启动，那么只需要在作业池中更新挂载信息即可
            // 2. 遍历所有删除的节点和连线，更新作业池内容
            // 2.1 删除的节点需要进行标记停止，删除前去后继线
            // 3. 变更任务连线类型

            // 上述几个流程处理完毕后，重新扫描一遍作业任务，调整动差因子
            List<Connector> changedConnectors = updatePkg.getChangedConnectors();
            List<Connector> newConnectors = updatePkg.getNewConnectors();
            List<Connector> delConnectors = updatePkg.getDelConnectors();

            List<IEntry> newEntries = updatePkg.getNewEntries();

            List<IEntry> deleteEntries = updatePkg.getDeleteEntries();

            // 更新作业池数据
            executorMap.put(jobId, job);
        } else {
            return false;
        }

        return false;
    }

    private void mountNewEntry(Job job, IEntry entry, List<Connector> inDegree, List<Connector> outDegree) {
        String nodeId = entry.getNodeId();
        Set<String> precursorEntries = new HashSet<String>();
        Set<String> successorEntries = new HashSet<String>();

        for (Connector connector : inDegree) {
            if (!connector.getTargetId().equals(nodeId)) {
                throw new IllegalArgumentException("The in degrees must be to: " + nodeId);
            }
            precursorEntries.add(connector.getSourceId());
        }
        for (Connector connector : outDegree) {
            if (!connector.getSourceId().equals(nodeId)) {
                throw new IllegalArgumentException("The out degrees must be from: " + nodeId);
            }
            successorEntries.add(connector.getTargetId());
        }

        // 判断前驱后继存在与否，如果直接前驱存在，直接挂载，如果直接前驱不存在，需要优先创建其前驱节点和关联关系

    }

    /**
     * 线性执行一个执行器（平行上一个节点）
     *
     * @param currExec    当前即将执行的任务节点
     * @param lastContext 上一个执行的任务的上下文信息
     * @return 返回当前执行器的数量和
     */
    @Override
    public int executeMissionLinear(AbstractExecutor currExec, ExecuteContext lastContext) throws ExecuteException {
        String lastExecuteId = (String) lastContext.getAttribute(ATTR_EXECUTE_ID);
        boolean isJobInstance = currExec instanceof AbstractJobExecutor;
        boolean isJobBlock = isJobInstance && ((Job) currExec.getEntry()).getIsObstructive();

        // 标记子作业属性
        currExec.getContext().addAttribute(ATTR_IS_CHILD_JOB, currExec instanceof AbstractJobExecutor);
        ExecuteContext topContextByKey = lastContext.getTopContextByKey((String) lastContext.getAttribute(ATTR_EXECUTE_PARENT_ID));
        if (currExec instanceof EndExecutor && topContextByKey != null && (Boolean) topContextByKey.getAttribute(ATTR_IS_CHILD_JOB)) {
            currExec.getContext().addAttribute(ATTR_IS_CHILD_JOB, true);
        }

        if (currExec.validate()) {
            if (StringUtil.isNotEmpty(lastExecuteId)) {

                if (currExec instanceof AbstractBlockingExecutor || isJobBlock) {
                    // 阻塞型的执行器上下文信息，每次执行一个执行器直接添加一个新的执行上下文信息
                    currExec.getContext().addFluxContext(lastExecuteId, lastContext);
                } else {
                    // 非阻塞型的执行器，每一条线穿过的时候都会添加到通量节点的执行器上下文信息中
                    ContextMap contextInCache = contextCache.get(currExec.getExecuteId());
                    if (contextInCache != null) {
                        contextInCache.put(lastExecuteId, lastContext);
                        currExec.getContext().setFluxContext(contextInCache);
                    } else {
                        currExec.getContext().addFluxContext(lastExecuteId, lastContext);
                    }
                    contextCache.put(currExec.getExecuteId(), currExec.getContext().getFluxContext());
                }


                String parentExecuteId = (String) lastContext.getAttribute(ATTR_EXECUTE_PARENT_ID);
                if (parentExecuteId != null && !isJobInstance) {
                    // 设置上一层执行器的上下文
                    currExec.getContext().addTopContext(parentExecuteId, lastContext.getTopContextByKey(parentExecuteId));
                    currExec.setParentExecuteId(parentExecuteId);
                }
                // 这里对子作业的父执行节点id进行处理，否则子作业中的节点去获得父节点是不正确的
                if (isJobInstance) {
                    currExec.setParentExecuteId(currExec.getExecuteId());
                }

            }

            executorMap.put(currExec.getExecuteId(), currExec.getEntry());
            Integer nodeType = currExec.getEntry().getNodeType();
            String threadName = "EUREKA-" + (nodeType == null ? "TYPE_UNDEFINED" : nodeType) + "-" + currExec.getExecuteId();
            Thread thread = new Thread(currExec, threadName);
            thread.start();

            currExec.getContext().addAttribute(ATTR_THREAD_ID, thread.getId());
        }
        return executorMap.size();
    }

    /**
     * 作为一个子节点执行下一个执行器（分父子层）
     *
     * @param currExec    需要执行的执行器任务
     * @param lastContext 上一个执行的任务的上下文信息
     * @return 返回当前执行器数目和
     */
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
            executorMap.put(currExec.getExecuteId(), currExec.getEntry());
            Integer nodeType = currExec.getEntry().getNodeType();
            String threadName = "EUREKA-" + (nodeType == null ? "TYPE_UNDEFINED" : nodeType) + "-" + currExec.getExecuteId();
            Thread thread = new Thread(currExec, threadName);
            thread.start();

            currExec.getContext().addAttribute(ATTR_THREAD_ID, thread.getId());
        }
        return executorMap.size();
    }

    /**
     * 作为下一层执行下一个执行器
     *
     * @param abstractExecutor 需要执行的Job执行器
     * @return 返回当前执行器数目总和
     */
    @Override
    public int executeMissionAsChild(AbstractExecutor abstractExecutor) throws ExecuteException {
        return executeMissionAsChild(abstractExecutor, null);
    }

    /**
     * 作为下一层执行下一个执行器
     *
     * @param entry 需要生成的执行器
     * @return 返回当前执行器数目总和
     */
    @Override
    public int executeMissionAsChild(IEntry entry) throws ExecuteException {
        return executeMissionAsChild(generateExecutor(entry));
    }

    /**
     * 根据执行器生成元素，生成一个执行器对象实例
     *
     * @param entry 需要创建的执行器的构建元素
     * @return 返回对应执行器实例（可运行）
     */
    @Override
    public AbstractExecutor generateExecutor(IEntry entry) throws ExecuteException {
        return generateExecutor(entry, null);
    }

    /**
     * 根据对应的元素以及父层执行器ID，生成一个执行器
     *
     * @param entry    需要执行的元素，可能是文件节点、FTP节点等
     * @param parentId 执行器的父ID（指的是作业号）
     * @return 使用父层执行器ID生成一个执行器实例
     */
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

    /**
     * 根据执行器的ID判断执行器是否存在
     *
     * @param abstractExecutor 需要添加判断的执行器
     * @return 如果执行器ID对应存在执行器已经在运行，那么返回值为true，否则返回值为false
     */
    @Override
    public boolean executorIsExisted(AbstractExecutor abstractExecutor) {
        return executorMap.containsKey(abstractExecutor.getExecuteId());
    }

    @Override
    public Map<String, Integer> getPreCountDownData() {
        return preCountDownData;
    }

    @Override
    public int getPreCountDownCnt(String entryId) {
        int cnt;
        RLock lock = distToolkit.getLock(EurekaSystemRedisKey.EUREKA$LOCKER_PER_CDL, 0);
        lock.lock();
        // 获得一个阻塞对象的动差阻塞因素，加锁同步（什么是动差？指在节点执行的过程中，拥有两个及以上的支路可以到达同一个
        // 下一节点，但某个支路因为错误或者判定不再继续执行下去，就需要为下一个节点的可执行条件进行降值，这个降值的数值就是
        // 动差的值）
        cnt = preCountDownData.get(entryId) == null ? 0 : preCountDownData.get(entryId);
        lock.unlock();
        return cnt;
    }

    @Override
    public void addPreCountDownCnt(String entryId) {
        RLock lock = distToolkit.getLock(EurekaSystemRedisKey.EUREKA$LOCKER_PER_CDL, 0);
        lock.lock();
        IEntry entry = getExecutorById(entryId);
        if (entry != null) {
            RCountDownLatch countDownLatch = distToolkit.getCountDownLatch(EurekaSystemRedisKey.EUREKA$SYSTEM_CONDITION, entryId, 1);
            countDownLatch.countDown();
        } else {
            Integer countDownCnt = preCountDownData.get(entryId);
            if (countDownCnt == null) {
                countDownCnt = 0;
            }
            preCountDownData.put(entryId, ++countDownCnt);
            logger.debug("降值节点：" + entryId + "(" + countDownCnt + ")");
        }
        lock.unlock();
    }

    @Override
    public int getExecutorSize(Class<? extends AbstractExecutor> clazz) {
        int cnt = 0;
        for (Object entry : executorMap.values()) {
            if (((IEntry) entry).getExecutorClz().equals(clazz)) {
                cnt++;
            }
        }
        return cnt;
    }

    @Override
    public void remove(AbstractExecutor abstractExecutor) {
        executorMap.remove(abstractExecutor.getExecuteId());
    }

    @Override
    public IEntry getExecutorById(String executorId) {
        return executorId == null ? null : (IEntry) executorMap.get(executorId);
    }

    @Override
    public void bindingExecutingListener(IExecutorListener listener) {
        executorListener = listener;
    }
}
