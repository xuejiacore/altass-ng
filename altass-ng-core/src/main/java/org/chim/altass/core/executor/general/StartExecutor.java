/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.executor.node.general
 * Author: Xuejia
 * Date Time: 2016/12/27 19:03
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.executor.general;


import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.annotation.RuntimeAutowired;
import org.chim.altass.core.constant.ExecutorAbility;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.buildin.attr.StartNodeConfig;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractStreamNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.core.executor.minirun.MiniRunnable;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Class Name: StartExecutor
 * Create Date: 2016/12/27 19:03
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 *
 * <font color="#0099cc">[开始节点执行器]</font>
 * <p>
 * - 流式处理
 * - 数据分片
 */
@Executable(name = "startNode", assemble = true, ability = {ExecutorAbility.ABILITY_STREAMING, ExecutorAbility.ABILITY_DISTRIBUTION})
@Resource(name = "开始节点", clazz = StartExecutor.class, midImage = "res/images/node/node-start.png")
public class StartExecutor extends AbstractStreamNodeExecutor implements MiniRunnable.DataCallback {

    @RuntimeAutowired
    private StartNodeConfig startNodeConfig = null;

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public StartExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    public void onPause() throws ExecuteException {

    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    @Override
    public void rollback(StreamData data) {

    }

    @Override
    public StreamData onStreamProcessing(byte[] data) throws ExecuteException {
        return new StreamData();
    }

    @Override
    public boolean onNodeNormalProcessing() throws ExecuteException {
        // 管道流延时启动的延时事件，单位为ms
        int delay = calculateStreamLayer() * START_DELAY;

        // 延时启动的时间
        new Timer().schedule(new TimerTask() {

            @Override
            @SuppressWarnings("Duplicates")
            public void run() {
                try {
                    altassChannel.publish(new StreamData(entry.getNodeId(), "STARTED"));

                    // =================================================================================================
                    try {
                        if (startNodeConfig == null) {
                            pushData(new StreamData(StartExecutor.this.entry.getNodeId(), "SKIP"));
                        } else {
                            Class<? extends MiniRunnable> runnableClz = startNodeConfig.getRunnableClz();
                            if (runnableClz != null) {
                                MiniRunnable runnable = runnableClz.newInstance();
                                runnable.setRunParamMap(startNodeConfig.getRunnableParamMap());
                                runnable.setDataCallback(StartExecutor.this);
                                runnable.run();
                            }
                        }
                    } catch (ExecuteException e) {
                        e.printStackTrace();
                    }
                    // =================================================================================================

                    streamLatch.countDown();
                    Thread.sleep(100);
                    if (streamingInfo.isDistributeNext()) {
                        for (IEntry successorEntry : streamingInfo.getStreamSuccessorIdxMap().values()) {
                            Integer cnt = streamingInfo.getPushDataCntMap().get(successorEntry.getNodeId());
                            altassChannel.publish(new StreamData(entry.getNodeId(), "FINISHED", cnt));
                        }
                    } else {
                        int dataPushCount = streamingInfo.getDataPushCount();
                        altassChannel.publish(new StreamData(entry.getNodeId(), "FINISHED", dataPushCount));
                    }
                } catch (InterruptedException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }

        }, delay);
        return true;
    }

    @Override
    public boolean isStreamingProcessing() throws ExecuteException {
        return false;
    }

    @Override
    public boolean retryIfFail() throws ExecuteException {
        return false;
    }

    @Override
    public void onDataFlush(Object data) throws ExecuteException {
        pushData(new StreamData(this.entry.getNodeId(), null, data));
    }
}
