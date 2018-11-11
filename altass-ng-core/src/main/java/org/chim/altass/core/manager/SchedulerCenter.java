package org.chim.altass.core.manager;

import org.chim.altass.core.exception.ExecuteException;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Class Name: SchedulerCenter
 * Create Date: 2017/9/11 16:39
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class SchedulerCenter {
    private Scheduler scheduler = null;

    private static SchedulerCenter schedulerCenter = null;

    /**
     * 初始化调度中心
     */
    private SchedulerCenter() {
        try {
            this.scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得 Quartz 调度中心的单例
     *
     * @return 调度中心
     */
    public static SchedulerCenter getInstance() {
        if (schedulerCenter == null) {
            synchronized (SchedulerCenter.class) {
                if (schedulerCenter == null) {
                    schedulerCenter = new SchedulerCenter();
                }
            }
        }
        return schedulerCenter;
    }

    public Scheduler getSchedule() {
        return scheduler;
    }

    /**
     * 构建一个作业
     *
     * @param job        实现了作业接口的待执行作业
     * @param name       作业名
     * @param group      作业分组
     * @param jobDataMap 作业数据
     * @return 作业
     * @throws SchedulerException -
     */
    public JobDetail buildJobDetail(IExecutableJob job, String name, String group, JobDataMap jobDataMap) throws SchedulerException {
        JobBuilder jobBuilder = JobBuilder.newJob(QuartzJob.class);
        jobBuilder.withIdentity(name, group);

        if (jobDataMap == null) {
            jobDataMap = new JobDataMap();
        }

        jobDataMap.put("JOB_EXECUTOR_INTERFACE", job);
        jobBuilder.setJobData(jobDataMap);
        return jobBuilder.build();
    }

    public Date schedualIt(JobDetail jobDetail, Trigger trigger) {
        try {
            return this.scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void schedule() throws SchedulerException {
        this.scheduler.start();
    }

    public interface IExecutableJob {
        void execute(JobExecutionContext context) throws ExecuteException;
    }

}
