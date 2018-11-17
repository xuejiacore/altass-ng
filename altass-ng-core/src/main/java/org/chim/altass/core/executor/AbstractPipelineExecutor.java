package org.chim.altass.core.executor;

import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.exception.ExecuteException;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/**
 * Class Name: PipelineExecutor
 * Create Date: 2017/10/24 13:22
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 抽象管道流输入执行器
 * <p>
 * 已经实现了关于数据发送传输以及节点调度的各种方法，子类只需要实现当前抽象类的真正执行方法即可
 *
 * @see AbstractPipelineExecutor#dataSource()
 */
public abstract class AbstractPipelineExecutor extends AbstractStreamNodeExecutor {

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public AbstractPipelineExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }


    /**
     * 核心方法：
     * <p>
     * 管道输入流需要处理的数据，推送数据必须要使用 推方法 pushData
     * pushData方法有对推流数据的次数统计，接收端会根据统计次数进行数据完整性进行校验
     * <p>
     * 该抽象方法有子类实现，用于数据获取、加工、传输
     *
     * @see AbstractPipelineExecutor#pushData(StreamData)
     */
    protected abstract void dataSource() throws ExecuteException;

    @Override
    protected final boolean onChildInit() throws ExecuteException {
        streamLatch = new CountDownLatch(1);
        return this.onPipelineInit();
    }

    protected abstract boolean onPipelineInit() throws ExecuteException;

    /**
     * 在流式节点初始化节点检查，发现直接后继不包含流式处理节点
     *
     * @throws ExecuteException 执行异常
     */
    @Override
    protected void notFoundStreamSuccessor() throws ExecuteException {
        // 此处必须知道当前管道流的直接后继，判断直接后继集中是否至少包含一个及以上的管道特性的节点，如果没有，说明后继
        // 节点中，没有可以用来接收流式数据的节点
        throw new ExecuteException("管道流的直接后继至少拥有一个流式处理特性的节点");
    }

    /**
     * 流式节点初始化节点检查，发现直接前驱包含流式处理节点
     *
     * @throws ExecuteException 执行异常
     */
    @Override
    protected void foundStreamPrecursor() throws ExecuteException {
        // 此处必须知道当前管道流的直接前驱，判断直接前驱集中是否有包含有管道特性的节点，如果是，那么当前节点是不允许被
        // 初始化和运行的，直接给出异常
        throw new ExecuteException("不允许管道流直接前驱节点拥有流式处理特性");
    }

    /**
     * 管道流输入节点处理，开启、关闭流事件以及数据完整性校验
     * <p>
     * 由于推流节点是一个异步节点，后继节点不会等待当前节点完全执行完成后才继续执行，因此采用计时器延时启动管道流节点
     *
     * @return 如果处理成功，那么返回值为true，否则返回值为false
     * @throws ExecuteException 执行异常
     */
    @Override
    public final boolean onNodeNormalProcessing() throws ExecuteException {

        // 管道流延时启动的延时事件，单位为ms
        // 延时启动的目的是为了是当前节点作为一个异步节点，后继节点的执行，不需要等待当前节点完全结束后再进行，延时的计算
        // 方式为：以当前节点为起始节点，计算后继节点包含有流式特性的节点的深度，每增加一层，延时250ms
        long delay = this.calculateStreamLayer() * START_DELAY;

        // 延时启动的时间
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    StreamData data;
                    altassChannel.publish(new StreamData(entry.getNodeId(), "STARTED"));
                    Thread.sleep(100);

                    // =================================================================================================
                    // 管道流推流核心方法
                    dataSource();
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
                } catch (InterruptedException | ExecuteException e) {
                    e.printStackTrace();
                }
            }

        }, delay);

        return true;
    }

    // =================================================================================================================
    // TODO:以下为中断恢复机制，后续实现
    // =================================================================================================================

    // 管道流暂停记录点
    @Override
    public void onPause() throws ExecuteException {
    }

    // 管道流恢复
    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    // =================================================================================================================
    // 当前管道节点的直接前驱一定不会有流式处理节点，以下方法均是对直接前驱包含流式处理的特性，在管道流中是无效的
    // =================================================================================================================

    @Deprecated
    @Override
    public final void onStreamOpen(StreamData data) throws ExecuteException {
        // 管道流的直接前驱一定不包含流式处理节点
    }

    @Deprecated
    @Override
    public final void onStreamProcessing(byte[] data) throws ExecuteException {
        // 管道流特性，不会流式处理上一个节点（上一个节点不能是流式节点）
        // 也就是当前节点是不可能进入当前方法的.
    }

    @Deprecated
    @Override
    public final boolean isStreamingProcessing() throws ExecuteException {
        // 管道流，不是一个流式处理节点，而是一个流式处理的起始，其直接前驱没有任何其他管道流
        return true;
    }

    @Deprecated
    @Override
    public final void onStreamClose(StreamData data) throws ExecuteException {
        // 管道流不监听该方法
    }

    @Deprecated
    @Override
    public final boolean retryIfFail() throws ExecuteException {
        // 管道流该方法无用
        return false;
    }

    @Override
    protected boolean onDisconnect() {
        throw new UnsupportedOperationException();
    }

}
