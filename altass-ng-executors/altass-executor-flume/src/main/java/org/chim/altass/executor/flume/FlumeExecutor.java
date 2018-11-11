package org.chim.altass.executor.flume;

import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.annotation.RuntimeAutowired;
import org.chim.altass.core.constant.ExecutorAbility;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractStreamNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.executor.flume.bean.FlumeConfig;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import javax.transaction.NotSupportedException;

/**
 * Class Name: FlumeExecutor
 * Create Date: 11/7/18 1:00 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Flume 接收节点，该节点支持关闭断流功能
 */
@Executable(name = "flumeExecutor", assemble = true, ability = {ExecutorAbility.ABILITY_STREAMING})
@Resource(name = "Flume", clazz = FlumeExecutor.class, midImage = "res/images/executor/flume_bg.png", pageUrl = "nodeConfigs/ext/flumeNodeConfig.jsp")
public class FlumeExecutor extends AbstractStreamNodeExecutor {

    @RuntimeAutowired
    private FlumeConfig flumeConfig = null;

    /**
     * To initialized executor
     *
     * @param executeId execute id
     */
    public FlumeExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void rollback(StreamData data) {

    }

    @Override
    protected void onArrange(UpdateAnalysis analysis) {

    }


    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    @Override
    public StreamData onStreamProcessing(byte[] data) throws ExecuteException {

        // 获取消费对象

        // TODO: 持续获得消息队列中的数据

        // 阻塞获取队列消息，需要进行加密验证，不允许所有的推送数据源都进入到后续的流程处理中

        return null;
    }

    @Override
    public boolean onNodeNormalProcessing() throws ExecuteException {
        throw new ExecuteException(new NotSupportedException("Unsupported processing."));
    }

    @Override
    public boolean retryIfFail() throws ExecuteException {
        return false;
    }

    @Override
    public boolean isStreamingProcessing() throws ExecuteException {
        return streamingInfo.isFoundStreamSuccessor();
    }
}
