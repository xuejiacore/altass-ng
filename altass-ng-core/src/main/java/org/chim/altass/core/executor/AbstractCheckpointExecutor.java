package org.chim.altass.core.executor;

import org.apache.commons.lang3.time.DateUtils;
import org.chim.altass.core.manager.SchedulerCenter;
import org.chim.altass.core.domain.buildin.attr.ATime;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.ExecuteException;
import org.quartz.*;

import java.util.Date;

/**
 * Class Name: AbstractCheckpointAbstractNodeExecutor
 * Create Date: 2017/9/11 15:54
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 检查点类型的执行器
 * <p>
 * 用于执行 Quartz 定时检查任务，直到达到某一个条件后才停止阻塞放行开始执行后面的节点
 * <p>
 * 通常用于循环等候一个网络或者数据存在的时候开始执行下一个任务的情况
 */
public abstract class AbstractCheckpointExecutor extends AbstractNodeExecutor implements SchedulerCenter.IExecutableJob {

    /**
     * 检查周期，所有的检查点类型的执行器都持有时间对象
     */
    protected ATime time = null;

    /**
     * 检查的重复次数
     */
    protected int repeatTimes = -1;

    /**
     * 执行的作业细节
     */
    private JobDetail jobDetail = null;

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    protected AbstractCheckpointExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    /**
     * 初始化检查点任务，初始化 Quartz 任务
     *
     * @return 如果初始化成功，那么返回值为true，否则返回值为false
     * @throws ExecuteException -
     */
    @Override
    public final boolean onInit() throws ExecuteException {
        this.time = ((Entry) entry).getTime();

        if (time == null) {
            throw new ExecuteException("检查周期不允许为空");
        }

        // 初始化一个 Quartz 任务
        JobDataMap jobDataMap = this.initJobData();
        try {
            this.jobDetail = SchedulerCenter.getInstance().buildJobDetail(this, executeId, getJobId(), jobDataMap);
        } catch (SchedulerException e) {
            throw new ExecuteException(e);
        }
        return true;
    }

    /**
     * 开始执行 Quartz 任务
     *
     * @return 如果启动成功，那么返回值为true，否则返回值为false
     */
    @Override
    public final boolean onStart() {
        Trigger trigger = this.obtainTrigger();
        // 启动 Quartz 任务
        Date date = SchedulerCenter.getInstance().schedualIt(jobDetail, trigger);
        onStarting();
        return date != null;
    }

    /**
     * 检查点执行处理等待
     *
     * @return -
     */
    @Override
    public final boolean onNodeProcessing() throws ExecuteException {
        try {
            SchedulerCenter.getInstance().schedule();
        } catch (SchedulerException e) {
            throw new ExecuteException(e);
        }
        try {
            EXECUTOR_LOGGER("msg", "进入检查点开始周期检查", "checkpoint", time);
            onCheckStart();
            await();
            onContinue();
            EXECUTOR_LOGGER("msg", "检查点检查通过，执行下一节点");
        } catch (InterruptedException e) {
            throw new ExecuteException(e);
        }
        return true;
    }

    /**
     * 节点开始，由于onStart final，因此提供该方法由子类重写
     */
    protected void onStarting() {
    }

    /**
     * 节点检查开始，由于onNodeProcessing final，因此提供该方法由子类重写
     */
    protected void onCheckStart() {
    }

    /**
     * 节点检查结束通过，由于onNodeProcessing final，因此提供该方法由子类重写
     */
    protected void onContinue() {
    }

    /**
     * 定时任务的执行点
     */
    @Override
    public final void execute(JobExecutionContext executionContext) throws ExecuteException {
        boolean checkpoint = checkpoint(executionContext);
        EXECUTOR_LOGGER("msg", "检查点节点周期检查", "checkResult", checkpoint);
        if (checkpoint) {
            conditionPassed();
            try {
                SchedulerCenter.getInstance().getSchedule().deleteJob(new JobKey(executeId, getJobId()));
            } catch (SchedulerException e) {
                throw new ExecuteException(e);
            }
        }
    }

    @Override
    public final void onFinally() throws ExecuteException {
        EXECUTOR_LOGGER("msg", "检查点周期检查通过，开始执行下一组节点", entry.obtainSuccessors());
        super.onFinally();
    }

    /**
     * 定时任务检查达成子类定义的条件后，开始
     */
    private void conditionPassed() {
        wakeup();
    }

    /**
     * 获得触发器实例，可由子类重写该触发器的获取
     *
     * @return 触发器实例
     */
    protected Trigger obtainTrigger() {

        // 执行周期
        long delay = time.getHour() * 3600000 + time.getMinutes() * 60000 + time.getSeconds() * 1000;
        delay = delay <= 0 ? 5 : delay;

        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInSeconds((int) (delay / 1000));

        if (repeatTimes <= 0) {
            simpleScheduleBuilder = simpleScheduleBuilder.repeatForever();
        }

        return TriggerBuilder.newTrigger()
                .withIdentity(executeId, Scheduler.DEFAULT_GROUP)
                .startAt(DateUtils.addSeconds(new Date(), 2))
                .withSchedule(simpleScheduleBuilder)
                .build();
    }

    @Override
    protected boolean onDisconnect() {
        throw new UnsupportedOperationException();
    }

    /**
     * 由条件检查子类自定义检查点
     *
     * @param context 作业上下文
     * @return 如果检查点检查通过，返回值true，放行当前阻塞节点，开始执行下一节点，否则返回值为false
     */
    protected abstract boolean checkpoint(JobExecutionContext context);

    /**
     * 获得作业执行数据，由子类自行定义作业数据
     *
     * @return 获得 Quartz 的作业数据
     */
    protected abstract JobDataMap initJobData();

}
