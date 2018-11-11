/*
 * Project: x-framework
 * Package Name: org.ike.service.job
 * Author: Xuejia
 * Date Time: 2017/2/10 15:29
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.smartui.service.impl;

import org.chim.altass.core.domain.Job;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.manager.CentersManager;
import org.chim.altass.smartui.service.IJobOpService;
import org.springframework.stereotype.Service;

/**
 * Class Name: JobOpServiceImpl
 * Create Date: 2017/2/10 15:29
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 作业操作服务实现
 */
@Service("jobOpService")
public class JobOpServiceImpl implements IJobOpService {

    @Override
    public boolean runningJob(Job job) {
        System.err.println("服务端接受到节点信息：" + job);
        try {
            CentersManager.getInstance().getMissionScheduleCenter().executeMissionAsChild(job);
        } catch (ExecuteException e) {
            e.printStackTrace();
        }
        return true;
    }
}
