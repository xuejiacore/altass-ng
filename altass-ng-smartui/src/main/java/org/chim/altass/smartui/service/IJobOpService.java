/*
 * Project: x-framework
 * Package Name: org.ike.etl.service.core
 * Author: Xuejia
 * Date Time: 2017/2/10 15:25
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.smartui.service;

import org.chim.altass.core.domain.Job;

/**
 * Class Name: IJobOpService
 * Create Date: 2017/2/10 15:25
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 作业操作服务
 */
public interface IJobOpService {

    /**
     * 运行一个作业
     *
     * @param job 需要执行的作业
     * @return 如果作业运行成功，那么返回值为true，否则返回值为false
     */
    boolean runningJob(Job job);
}
