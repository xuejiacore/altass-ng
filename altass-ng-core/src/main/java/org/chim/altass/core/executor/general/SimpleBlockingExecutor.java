/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.executor.node.general
 * Author: Xuejia
 * Date Time: 2017/1/14 15:21
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.executor.general;


import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractBlockingExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.toolkit.job.UpdateAnalysis;

/**
 * Class Name: SimpleBlockingExecutor
 * Create Date: 2017/1/14 15:21
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * <font color="#0099cc">[简单的汇聚阻塞装置]</font><br/>
 * 简单阻塞节点能够产生等待入度节点全部执行完成后通过的效果，一般用于汇聚等待数据或文件，或合并对象使用
 */
@Executable(name = "simpleBlocking", assemble = true)
@Resource(name = "多路归并", clazz = SimpleBlockingExecutor.class, midImage = "res/images/node/aggregation_bg.png")
public class SimpleBlockingExecutor extends AbstractBlockingExecutor {
    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public SimpleBlockingExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    public boolean onNodeProcessing() throws ExecuteException {
        logger.debug("简单阻塞节点处理中");
        return true;
    }

    @Override
    protected void onInitBeforeAwait() {

    }

    @Override
    protected boolean onInitAfterWakeup() {
        logger.debug("简单阻塞节点阻塞结束，继续执行");
        return true;
    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    @Override
    public void finished() throws ExecuteException {
        super.finished();
        logger.info("简单阻塞节点处理完成");
    }

    @Override
    public void onPause() throws ExecuteException {

    }
}
