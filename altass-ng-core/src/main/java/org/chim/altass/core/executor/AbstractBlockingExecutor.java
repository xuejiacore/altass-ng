/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.executor
 * Author: Xuejia
 * Date Time: 2016/12/28 10:12
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.executor;


import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.Job;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.general.EndExecutor;
import org.chim.altass.core.executor.general.SimpleBlockingExecutor;
import org.chim.altass.core.executor.general.StartExecutor;
import org.chim.altass.toolkit.RedissonToolkit;
import org.redisson.api.RLock;
import static org.chim.altass.core.constant.ExecutorAttr.*;

import java.util.List;

/**
 * Class Name: AbstractBlockingExecutor
 * Create Date: 2016/12/28 10:12
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 阻塞型执行器 <font color="#0099cc">[汇聚型节点处理器]</font>
 * <p>
 * 该类执行器的作用是能够单向阻塞入度，只有当所有的入度都已经完成之后，才会执行当前节点
 * <p>
 * 目前已实现的阻塞型节点有如：<br/>
 * {@link EndExecutor}<br/>
 * {@link SimpleBlockingExecutor}
 */
@SuppressWarnings("Duplicates")
public abstract class AbstractBlockingExecutor extends AbstractNodeExecutor {
    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    protected AbstractBlockingExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    /**
     * 阻塞执行器的初始化方法为final，这部分已被用于阻塞控制使用，初始化的操作有子类实现两个抽象方法
     *
     * @return 如果初始化成功，那么返回值为true，否则返回值为false
     * @throws ExecuteException 如果阻塞中断，则抛出异常
     */
    @Override
    public final boolean onInit() throws ExecuteException {
        onInitBeforeAwait();
        EXECUTOR_LOGGER("msg", "阻塞型节点预初始化，加锁阻塞中");
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new ExecuteException(e);
        }
        EXECUTOR_LOGGER("msg", "阻塞型节点正在初始化，解锁运行中");
        return onInitAfterWakeup();
    }

    /**
     * 阻塞型执行器该方法已经废弃，不适用
     *
     * @return 抛出异常
     */
    @Override
    public final boolean wakeup() {
        this.latch.countDown();
        return true;
    }

    /**
     * 阻塞型执行器唤醒
     *
     * @param fromContext 主动唤醒执行器的上下文信息
     * @return 如果唤醒成功，返回值为true，否则返回值为false
     */
    @Override
    public final boolean wakeup(ExecuteContext fromContext) {
        this.latch.countDown();
        this.onWakeup(fromContext);
        return true;
    }

    @Override
    public synchronized boolean validate() {
        return missionScheduleCenter.getExecutorById(executeId) == null && !willExecExecutor.contains(this.entry);
    }

    /**
     * 设置当前阻塞型执行器的前驱节点元素，根据前驱节点元素的数量将会自动设定阻塞解阻塞的条件
     *
     * @param entry 当前执行器绑定的执行元素
     */
    @Override
    public final void setEntry(IEntry entry) throws ExecuteException {
        super.setEntry(entry);
        // 由当前节点测算的阻塞因素是不需要进行同步的（线路是静态的）
        int blockAlpha = calculateBlockAlpha(this.entry);
        /*
         * 注意：
         *
         * 在进行阻塞因子计算的时候，防止此时其它线程正在生成当前BlockingExecutor，导致解锁次数与因为运行时线路切换导致的
         * 动态阻塞因素差出现不同步现象，因此，在生成一个Executor的时候，必须进行实例 lock 层面上的锁定，以此同步解锁次数
         *
         * 此同步与org.ike.eurekacore.core.executor.AbstractNodeExecutor.finished 中的同步保持数据一致性
         *
         * 2017-01-17 18:00:14
         * -- By Xuejia
         */
        int preCountDownCnt;
        int countDownLatchCnt;
        RLock lock = distToolkit.getLock(EurekaSystemRedisKey.EUREKA$LOCKER_PER_CDL, 0);
        lock.lock();
        // 锁的数量减一的原因是当前已经创建，唤醒的时候当前节点不会唤醒本身，减getPreCountDownCnt（运行时预判阻塞因子动差）
        preCountDownCnt = missionScheduleCenter.getPreCountDownCnt(entry.getNodeId());
        countDownLatchCnt = (blockAlpha - 1) - preCountDownCnt;
        missionScheduleCenter.getPreCountDownData().remove(entry.getNodeId());
        lock.unlock();
        logger.trace("节点 [" + getExecuteId() + "] 阻塞动差推演\tBlockAlpha：" + blockAlpha + "\tPreCountDownCnt：" + preCountDownCnt + "\tCountDownLatch Count： " + countDownLatchCnt);
        // 阻塞值为0，执行完当前节点后将不会进行阻塞
        if (countDownLatchCnt == 0) {
            // 防止不阻塞情况下没有释放阻塞的情况
            missionScheduleCenter.releaseBlocking(entry.getNodeId());
        }
        this.latch = RedissonToolkit.getInstance().getCountDownLatch(EurekaSystemRedisKey.EUREKA$SYSTEM_CDL, executeId, countDownLatchCnt);
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
    public void onWakeup(ExecuteContext fromContext) {
        this.context.addFluxContext((String) fromContext.getAttribute(ATTR_EXECUTE_ID), fromContext);
    }

    @Override
    public void finished() throws ExecuteException {
        super.finished();
        this.clearCache();
    }

    @Override
    public void clear() {
        super.clear();
        this.clearCurrentNodeCache();
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
