/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.executor.node.general
 * Author: Xuejia
 * Date Time: 2016/12/28 11:04
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
 * Class Name: EndAbstractNodeExecutor
 * Create Date: 2016/12/28 11:04
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * <font color="#0099cc">[结束节点执行器]</font>
 */
@Executable(name = "endNode", assemble = true)
@Resource(name = "结束节点", clazz = EndExecutor.class, midImage = "res/images/node/node-end.png")
public class EndExecutor extends AbstractBlockingExecutor {
    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public EndExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    protected void onInitBeforeAwait() {
        logger.trace("预初始化");
    }

    @Override
    protected boolean onInitAfterWakeup() {
        logger.trace("正式初始化");
        return true;
    }

    @Override
    public boolean onNodeProcessing() {
        logger.trace("处理结束节点,模拟0.2s处理...");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    @Override
    public void finished() throws ExecuteException {
        super.finished();
        this.clearCache();
        this.clearDistCache(getCurrentJob().getStartNode().getNodeId());
        this.getJobExecutor().wakeup();
    }

    @Override
    public void clear() {
        super.clear();
        this.clearCurrentNodeCache();
    }

    @Override
    public void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    public void onPause() throws ExecuteException {

    }
}
