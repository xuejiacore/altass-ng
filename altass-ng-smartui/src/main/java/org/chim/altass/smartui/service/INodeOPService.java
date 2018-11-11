/*
 * Project: x-framework
 * Package Name: org.ike.etl.service.core
 * Author: Xuejia
 * Date Time: 2017/2/8 14:28
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.smartui.service;


import org.chim.altass.core.domain.Job;

import java.util.List;

/**
 * Class Name: INodeOPService
 * Create Date: 2017/2/8 14:28
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * 节点存储操作接口
 * <p>
 * 完成节点信息的读写
 */
public interface INodeOPService {
    /**
     * 创建一个作业
     *
     * @param job 作业实体
     * @return 如果作业创建成功，那么返回值为true，否则返回值为false
     */
    boolean createJob(Job job);

    /**
     * 根据作业的ID获得作业实体
     *
     * @param jobId 需要获得的作业ID
     * @return 如果获得作业成功，那么返回作业实体，否则返回值为null
     */
    Job getJobById(String jobId);

    /**
     * 更新作业
     *
     * @param job 需要更新的作业实体
     * @return 如果作业更新成功，那么返回值为true，否则返回值为false
     */
    boolean updateJob(Job job);

    /**
     * 移除一个作业
     *
     * @param jobId 需要移除的作业ID
     * @return 如果移除成功，那么返回值为true，否则返回值为false
     */
    boolean removeJob(String jobId);

    /**
     * 获得作业列表
     *
     * @return 返回查询得到的作业列表
     */
    List<Job> listJobs();
}
