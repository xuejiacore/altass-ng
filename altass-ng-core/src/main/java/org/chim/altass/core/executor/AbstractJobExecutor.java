/*
 * Project: x-framework
 * Package Name: org.ike.etl.core.executor
 * Author: Xuejia
 * Date Time: 2016/12/16 11:51
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.executor;


import org.chim.altass.core.ansi.AnsiColor;
import org.chim.altass.core.ansi.AnsiOutput;
import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.Node;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.face.IJobEvent;
import org.chim.altass.core.executor.face.IJobLog;
import org.chim.altass.core.executor.general.StartExecutor;
import org.chim.altass.toolkit.JDFWrapper;
import org.redisson.api.RLock;

import java.util.HashMap;
import java.util.List;

import static org.chim.altass.core.constant.ExecutorAttr.*;

/**
 * Class Name: AbstractJobExecutor
 * Create Date: 2016/12/16 11:51
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@SuppressWarnings("Duplicates")
public abstract class AbstractJobExecutor extends AbstractExecutor implements IJobEvent, IJobLog {

    /**
     * 当前作业执行器持有的作业
     */
    private Job job = null;

    /**
     * 初始化一个执行器
     *
     * @param executeId 执行id
     */
    public AbstractJobExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void setEntry(IEntry entry) throws ExecuteException {
        super.setEntry(entry);
        if (((Job) entry).getIsObstructive()) {
            Job currentJob = getCurrentJob();
            if (currentJob != null) {
                int blockAlpha = calculateBlockAlpha(entry);
                int preCountDownCnt;
                int countDownLatchCnt;

                RLock lock = distToolkit.getLock(EurekaSystemRedisKey.EUREKA$LOCKER_PER_CDL, 0);
                lock.lock();
                // 锁的数量减一的原因是当前已经创建，唤醒的时候当前节点不会唤醒本身，减getPreCountDownCnt（运行时预判阻塞因子动差）
                preCountDownCnt = missionScheduleCenter.getPreCountDownCnt(entry.getNodeId());
                countDownLatchCnt = (blockAlpha - 1) - preCountDownCnt;
                missionScheduleCenter.getPreCountDownData().remove(entry.getNodeId());
                lock.unlock();

                logger.trace("阻塞因子 BlockAlpha：" + blockAlpha + " PreCountDownCnt：" + preCountDownCnt + " [" + getExecuteId() + "] CountDownLatch Count： " + countDownLatchCnt);
                // 阻塞值为0，执行完当前节点后将不会进行阻塞
                if (countDownLatchCnt == 0) {
                    // 防止不阻塞情况下没有释放阻塞的情况
                    missionScheduleCenter.releaseBlocking(entry.getNodeId());
                }
                this.latch = this.distToolkit.getCountDownLatch(EurekaSystemRedisKey.EUREKA$SYSTEM_CDL, executeId, countDownLatchCnt);
            }
        }
    }


    @Override
    public boolean beforeInit() throws ExecuteException {
        return true;
    }

    @Override
    public final boolean onInit() throws ExecuteException {
        // TODO：作业线程的初始化操作，子类实现初始化操作方法
        logger.trace(executeId + " 作业初始化");
        if ((Boolean) context.getAttribute(ATTR_IS_CHILD_JOB) && ((Job) entry).getIsObstructive()) {
            try {
                onInitBeforeAwait();
                latch.await();
                return onInitAfterWakeup();
            } catch (InterruptedException e) {
                throw new ExecuteException(e);
            }
        }
        /*
         * 检查最新的作业情况，更新到作业中心
         */
        return true;
    }

    @Override
    protected void customSystemVariables(HashMap<Object, Object> systemVariables) {
        systemVariables.put("isJobTag", true);
    }

    @Override
    public boolean onStart() {
        // TODO：作业线程初始化完成之后，调用到onStart方法，这里可以最后对启动前做一些操作
        logger.trace(executeId + " 作业开始");
        /*
         * 获取当前启动的作业
         */
        this.job = (Job) this.entry;
        logger.trace(
                AnsiOutput.toString(AnsiColor.GREEN,
                        "\n\n\n\t\t$$$=================================作业[" + this.job.getJobId() +
                                "]开始运行=================================$$$\n\n\n", AnsiColor.DEFAULT
                ));
        return true;
    }

    @Override
    public boolean beforeProcess() throws ExecuteException {
        /*
         * 此处进行作业的查找、初始化工作
         */
        logger.trace(executeId + " 作业预处理");
        /*
         * 查找初始化成功后，返回true，继续进行processing
         * 按照正确的运行状态，不会存在查找失败的作业，如果获取不到作业，需要抛出异常
         */
        if (this.job == null) {
            logger.error("作业获取失败");
            throw new ExecuteException("作业获取失败");
        } else {
            // TODO：作业获取成功，获取开始节点下的其它下一节点信息
            logger.trace("作业获取成功，获得开始节点以及开始节点对应的下一节点和结束节点");
            Entry startNode = (Entry) job.obtainStartNode();
            Node endNode = job.obtainEndNode();

            List<IEntry> nextNodes = this.job.getEntries(startNode.obtainSuccessors());
            if (nextNodes == null || nextNodes.size() == 0) {
                throw new ExecuteException("不合法的开始节点");
            }

            String nextNodeInfo = "[";
            // TODO：作业中心作业池添加作业节点信息
            logger.trace("作业中心作业池添加作业节点信息：" + startNode.getNodeId() + "_" + startNode.getNodeType());
            logger.trace("当前准备队列：" + addToExecutingQueue(executeId, startNode, this));
            // TODO：作业监控中心启动对即将运行节点的监控
            logger.trace("作业监控中心启动对即将运行节点的监控：" + startNode.getNodeId() + "_" + startNode.getNodeType());
            nextNodeInfo += startNode.getNodeType() + ":" + startNode.getNodeId() + ", ";

            // 设置下一个节点的数量信息
            context.addAttribute(ATTR_NEXT_ENTRY_SIZE, 1);
            context.addAttribute(ATTR_NEXT_ENTRY_INFO, nextNodeInfo.substring(0, nextNodeInfo.length() - 2) + "]");
        }
        return true;
    }

    /**
     * 验证开始节点之后是否是直接的结束节点
     *
     * @param nextNodes 下一节点集合
     * @param endNode   结束节点
     * @return 如果满足作业节点要求，那么返回值为true，否则返回值为false
     */
    protected boolean validate(List<Entry> nextNodes, Node endNode) {
        // TODO：此处废弃
        for (IEntry nextNode : nextNodes) {
            if (((Entry) nextNode).getNodeId().equals(endNode.getNodeId())) {
                return false;
            }
        }
        return endNode.obtainPrecursors().size() >= 1;
    }

    @Override
    public boolean processing() throws ExecuteException {
        logger.trace("从作业池中获取当前作业的执行节点，启动这些节点执行");
        /*
         * 当前作业可能包含有大量的耗时节点，需要在处理完毕返回前加锁，等待子节点或者子作业执行完成返回
         */
        // TODO：这里是作业线程中真正进行的操作代码
        // TODO:作业级别的执行器不会轻易结束，以下延时模拟多个节点的执行状况
        IEntry entry;
        while ((entry = willExecExecutor.poll()) != null) {
            rpcService.runAsChild(new JDFWrapper(entry), executeId, executeId);
        }
        /*
         * 除非时所有的节点都已经执行完成（包含异常中断），否则processing不进行返回操作，从而使作业的状态一致保持在“进行中”
         */
        try {
            await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
         * 所有的节点/作业执行完成，解锁，继续运行
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
    public void afterProcess() {
        // TODO：在作业线程执行完毕之后的一些正常收尾工作
        logger.trace(executeId + " 作业处理结束");
    }

    @Override
    public void onFinally() {
        // TODO：作业线程执行的过程中可能会抛出运行时的异常，不管是否也有抛出异常，都会执行该方法
        // TODO:该方法需要对当前作业节点是否是作为一个阻塞型的子作业运行，如果作为子作业运行，那么和阻塞节点的处理相同
        logger.trace(executeId + " FINALLY");
    }

    @Override
    public void finished() throws ExecuteException {
        String parentJobId = this.job.getParentJobId();
        if ((Boolean) this.context.getAttribute(ATTR_IS_CHILD_JOB) && parentJobId != null) {
            IEntry parentJobExecutor = missionScheduleCenter.getExecutorById(parentJobId);
            List<IEntry> successors = this.job.getSuccessors();
            for (IEntry successor : successors) {
                rpcService.runAsChild(new JDFWrapper(successor), parentJobId,
                        parentJobExecutor.getNodeId());
//                logger.trace("调起子作业直接后继节点：[" + executor.getExecuteId() + "] 共 " + successors.size() + " 个");
            }
        }
        this.onSuccess();
        EXECUTOR_LOGGER("作业完成Finished", "JobId", getExecuteId());
        logger.trace(
                AnsiOutput.toString(AnsiColor.GREEN,
                        "\n\n\n\t\t$$$=================================作业[" + this.job.getJobId() +
                                "]运行结束=================================$$$\n\n\n", AnsiColor.DEFAULT
                ));
        this.clearCache();
    }

    @Override
    public void clear() {
        super.clear();
        this.clearCurrentNodeCache();
    }

    @Override
    public void onException(Exception e) {
        e.printStackTrace();
        onJobException(e);
    }

    @Override
    public void onSkip(String reason) {
        logger.trace("跳过作业:" + reason);
    }

    @Override
    public synchronized boolean validate() {
        boolean isBlockAndCheckNotInScheduleCenter = false;
        // 如果是作为子作业，并且作为子作业是阻塞的，那么不允许有相同的子作业同时在运行，当前作业相当于是一个block作业，只有所有的前驱
        // 都执行完成了，才会执行当前作业的核心部分
        if ((Boolean) context.getAttribute(ATTR_IS_CHILD_JOB) && ((Job) entry).getIsObstructive()) {
            isBlockAndCheckNotInScheduleCenter = missionScheduleCenter.getExecutorById(executeId) != null ||
                    willExecExecutor.contains(this.entry);
        }
        return !isBlockAndCheckNotInScheduleCenter;
    }

    @Override
    public boolean wakeup(ExecuteContext fromContext) {
        this.latch.countDown();
        logger.trace("[" + this.entry.getNodeId() + "] 被 [" + fromContext.getAttribute(ATTR_EXECUTE_ID) + "] 尝试唤醒");
        this.onWakeup(fromContext);
        return true;
    }

    /**
     * 递归计算阻塞因子
     * <p>
     * [Recursive Call]
     *
     * @param entry 需要进行计算的节点元素
     * @return 返回阻塞因子值
     */
    private int calculateBlockAlpha(IEntry entry) {
        int blockAlpha = 0;
        List<IEntry> entries = getCurrentJob().getEntries(entry.obtainPrecursors());
        for (IEntry e : entries) {
            Class<? extends AbstractExecutor> executorClz = e.getExecutorClz();
            if (executorClz == null) {
                return 1;
            } else {
                boolean haveMoreLastInDegree = e.getInDegree().size() != 0;
                if (!AbstractBlockingExecutor.class.isAssignableFrom(executorClz)
                        && !AbstractStreamNodeExecutor.class.isAssignableFrom(executorClz)
                        && (!(AbstractJobExecutor.class.isAssignableFrom(executorClz) && ((Job) e).getIsObstructive()))
                        && haveMoreLastInDegree) {
                    // 不是阻塞节点并且不是流式节点并且不是阻塞型子作业节点，当前阻塞节点要等待的次数对应锐减值 1 （因为一个阻塞/流式对于当前节
                    // 点而言只是等待一次，其余部分多算了）
                    blockAlpha += calculateBlockAlpha(e) - 1;
                }
                // 前驱与开始节点断链的，对应的局部阻塞因子为0
                if (haveMoreLastInDegree || StartExecutor.class.isAssignableFrom(e.getExecutorClz())) {
                    blockAlpha++;
                }
            }
        }
        return blockAlpha;
    }

    @Override
    protected boolean onDisconnect() {
        throw new UnsupportedOperationException();
    }

    /**
     * 阻塞前预初始化
     */
    protected abstract void onInitBeforeAwait();

    /**
     * 通过阻塞区后的正式初始化
     *
     * @return 如果初始化成功，那么返回值为true，否则返回值为false
     */
    protected abstract boolean onInitAfterWakeup();
}
