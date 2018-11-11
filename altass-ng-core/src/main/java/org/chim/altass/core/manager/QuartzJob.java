package org.chim.altass.core.manager;

import org.chim.altass.core.exception.ExecuteException;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Class Name: QuartzJob
 * Create Date: 2017-09-11 20:45:42
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
public class QuartzJob implements Job {

    public QuartzJob() {

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();

        SchedulerCenter.IExecutableJob executableJob = (SchedulerCenter.IExecutableJob) mergedJobDataMap.get("JOB_EXECUTOR_INTERFACE");
        if (executableJob != null) {
            try {
                executableJob.execute(context);
            } catch (ExecuteException e) {
                e.printStackTrace();
            }
        }
    }
}