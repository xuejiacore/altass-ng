/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.executor.node.general
 * Author: Xuejia
 * Date Time: 2017/1/14 1:38
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.executor.general;

import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.domain.buildin.attr.ATime;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.domain.meta.InputParam;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.toolkit.job.UpdateAnalysis;

/**
 * Class Name: TimerAbstractNodeExecutor
 * Create Date: 2017/1/14 1:38
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * <font color="#0099cc">[延时器]</font>
 */
@Executable(name = "timer", assemble = true)
@Resource(name = "延时器", clazz = TimerExecutor.class, midImage = "res/images/node/delay_bg.png", pageUrl = "nodeConfigs/general/timerNodeConfig.jsp")
public class TimerExecutor extends AbstractNodeExecutor {

    private ATime time = null;

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public TimerExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    public boolean onInit() throws ExecuteException {
        time = ((Entry) this.entry).getTime();
        return super.onInit();
    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    @Override
    public boolean onNodeProcessing() throws ExecuteException {
        long delay = time.getHour() * 3600000 + time.getMinutes() * 60000 + time.getSeconds() * 1000;
        logger.debug("延时器 ---->>>>>>>>>> " + delay + " ms");
        InputParam inputParam = entry.getInputParam();
        if (inputParam != null) {
            MetaData exception = inputParam.getParameter("exception");
            // 测试异常参数
            if (exception != null) {
                throw new ExecuteException(String.valueOf(exception.getValue()));
            }

            // 测试输入参数获取
            MetaData inputParamName = inputParam.getParameter("paramName");
            MetaData inputParamAge = inputParam.getParameter("paramAge");
            if (inputParamName != null) {
                logger.debug("入参：----->>>>>" + inputParamName.info());
            }
            // 测试输入参数加工
            if (inputParamAge != null) {
                // 测试输出参数
                addOutputParam(new MetaData(inputParamAge.getField() + "_out", String.valueOf(inputParamAge.getValue())));
            }
        }

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new ExecuteException(e);
        }
        return true;
    }

    @Override
    public void onPause() throws ExecuteException {

    }
}
