/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.executor.node.general
 * Author: Xuejia
 * Date Time: 2017/1/4 23:52
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.executor.general;


import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractJobExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.toolkit.job.UpdateAnalysis;

/**
 * Class Name: JobAbstractExecutor
 * Create Date: 2017/1/4 23:52
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * <font color="#0099cc">[作业执行器]</font>
 */
@Executable(name = "jobExecutor", assemble = true)
@Resource(name = "子作业", clazz = JobExecutor.class, midImage = "res/images/node/job_bg.png")
public class JobExecutor extends AbstractJobExecutor {
    /**
     * 初始化一个执行器
     *
     * @param executeId 执行id
     */
    public JobExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    public void onSuccess() {
        logger.trace("作业执行成功");
        logger.trace("当前作业数：" + missionScheduleCenter.getExecutorSize(AbstractJobExecutor.class));
    }

    @Override
    public void onJobException(Exception e) {
        logger.error("作业执行异常");
    }

    @Override
    public void onPause() {
        logger.trace("作业暂停");
    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    @Override
    protected void onInitBeforeAwait() {

    }

    @Override
    protected boolean onInitAfterWakeup() {
        return true;
    }
}
