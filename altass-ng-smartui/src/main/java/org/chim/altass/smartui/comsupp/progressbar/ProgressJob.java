/**
 * Project: x-framework
 * Package Name: org.ike.monitor.comsupp.progressbar
 * Author: Xuejia
 * Date Time: 2016/10/9 13:55
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.comsupp.progressbar;

import org.chim.altass.smartui.comsupp.face.IJob;

import java.util.Date;
import java.util.List;

/**
 * Class Name: ProgressJob
 * Create Date: 2016/10/9 13:55
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:需要执行的进度类型的作业数据
 */
public class ProgressJob<T> implements IJob {

    private JobMeta jobMeta = null;

    /**
     * 创建一个进度处理的作业
     *
     * @param jobId   作业的id
     * @param jobName 作业的名称
     * @param data    作业数据
     */
    public ProgressJob(String jobId, String jobName, List<T> data) {
        jobMeta = new JobMeta(jobId, jobName, data);
    }

    /**
     * 创建一个进度处理的作业
     *
     * @param jobName 作业的名称
     * @param data    作业数据
     */
    public ProgressJob(String jobName, List<T> data) {
        this(String.valueOf(System.currentTimeMillis()), jobName, data);
    }

    /**
     * 创建一个进度处理的作业
     *
     * @param data 作业数据
     */
    public ProgressJob(List<T> data) {
        this(String.valueOf(System.currentTimeMillis()), data);
    }

    /**
     * 作业元数据
     */
    private class JobMeta {
        public String jobId = null;             // 作业的id
        public String jobName = null;           // 作业名称
        public List<T> jobData = null;          // 作业数据
        public Date jobBeginDate = null;        // 作业的开始时间
        public Date jobEndDate = null;          // 作业的完成时间

        public JobMeta(String jobId, String jobName, List<T> jobData) {
            this.jobId = jobId;
            this.jobName = jobName;
            this.jobData = jobData;
        }

        public List<T> getJobData() {
            return jobData;
        }
    }

    public JobMeta getJobMeta() {
        return jobMeta;
    }

    public List<T> getJobData() {
        return getJobMeta().getJobData();
    }

    public void setJobMeta(JobMeta jobMeta) {
        this.jobMeta = jobMeta;
    }

    public static void main(String[] args) {
    }
}
