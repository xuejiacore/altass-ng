package org.chim.altass.core.executor;

import com.alibaba.fastjson.JSON;
import org.chim.altass.core.IPipeline;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.constant.StreamEvent;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.buildin.attr.ARegion;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.face.IStream;
import org.chim.altass.core.streaming.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/**
 * Class Name: AbstractStreamNodeExecutor
 * Create Date: 2017/10/24 15:18
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * An abstract stream node executor.
 * <p>
 * A streaming executor have all properties and functions those an abstract node executor have. It implements IPipeline
 * and IStream interface.
 * <p>
 * Such as an pipeline executor. An abstract streaming node executor could be degenerated as pipeline which
 * only contains pipeline interface and part of stream interface.
 * <p>
 * An full streaming executor contains pipeline properties to send streaming data and stream process properties which
 * make current node executor could receive data from precursor and quasi-realtime push processed data to successor.
 * <p>
 * Rollback data that current process fail is an another property of streaming node when process program return a null
 * stream data.
 *
 * @see IPipeline Pipeline interface
 * @see IStream Stream interface
 * @see TransactionListener TransactionListener interface
 */
public abstract class AbstractStreamNodeExecutor extends AbstractNodeExecutor
        implements IPipeline, IStream, TransactionListener {
    protected static final int START_DELAY = 250;                   // Delay of stream open. (ms)

    private String currentNodeId = null;                            // Current node id.

    protected CountDownLatch finallyLatch = null;                   // 控制后继节点是否能够启动
    /*
     * 控制流式操作是否完成，该锁释放后，各种资源即将被回收，为了防止异步处理的流式节点，其他资源被过早地回收，只有收到
     * 当前节点的所有直接前驱管道流均关闭后才会释放其他资源
     */
    protected CountDownLatch streamLatch = null;
    protected StreamingInfo streamingInfo;
    protected AbstractAltassChannel altassChannel = null;
    protected boolean previousIsFinished = false;
    protected ThreadLocal<Byte> groupMode = ThreadLocal.withInitial(() -> StreamEvent.EVENT_DATA);

    protected ThreadLocal<String> groupKey = ThreadLocal.withInitial(() -> "");

    /**
     * To initialize streaming executor.
     *
     * @param executeId execute id
     */
    public AbstractStreamNodeExecutor(String executeId) throws ExecuteException {
        super(executeId);
        streamingInfo = new StreamingInfo();
        altassChannel = new AltassRabbitChannel(streamingInfo, new Transaction(this));
    }

    /**
     * Check precursor and successor whether contains streaming executor.
     */
    @Override
    public boolean beforeNodeInit() {
        // 流式节点初始化
        streamingInfo.setEntry(this.entry);
        streamingInfo.setPrecursorEntries(getCurrentJob().getEntries(this.entry.obtainPrecursors()));
        streamingInfo.setSuccessorsEntries(getCurrentJob().getEntries(this.entry.obtainSuccessors()));
        return true;
    }

    /**
     * Streaming node will waiting for all push operation finished. Everything will be recycle after this method such as
     * logger, mission in schedule center and so on.
     */
    @Override
    protected void beforeRemoveFromMissionSchedule() throws ExecuteException {
        try {
            if (streamLatch != null)
                streamLatch.await();
        } catch (InterruptedException e) {
            throw new ExecuteException(e);
        }
        super.beforeRemoveFromMissionSchedule();
    }

    /**
     * This function will be invoke before onFinally method and after 'processing' function. The method named 'onFinally'
     * will start all successor of current executor in all executor. It means that all successor will be execute when
     * current method named 'beforeOnFinally' had finished and returned. Generally speaking, finallyLatch could be used to
     * controller when and whether successor could be ran.
     *
     * @throws ExecuteException -
     */
    @Override
    protected void beforeOnFinally() throws ExecuteException {
        if (finallyLatch != null) {
            try {
                finallyLatch.await();
            } catch (InterruptedException e) {
                throw new ExecuteException(e);
            }
        }
    }

    /**
     * To initialize a streaming node, including data queues and locker initialization.
     */
    @SuppressWarnings("Duplicates")
    @Override
    public final boolean onInit() throws ExecuteException {
        this.currentNodeId = entry.getNodeId();
        int precursorStreamCnt = 0;                            // Count of precursor streaming node.
        int successorStreamCnt = 0;                            // Count of successor streaming node.
        boolean foundStreamPrecursor = false;
        boolean foundStreamSuccessor = false;
        List<IEntry> precursorEntries = streamingInfo.getPrecursorEntries();
        List<IEntry> successorsEntries = streamingInfo.getSuccessorsEntries();

        boolean allIsTheSameRegion = true;
        String tmpRegion = null;

        // Detect precursor
        if (precursorEntries != null) {
            for (IEntry entry : precursorEntries) {
                Class<? extends AbstractExecutor> executorClz = entry.getExecutorClz();
                if (AbstractStreamNodeExecutor.class.isAssignableFrom(executorClz)) {
                    foundStreamPrecursor = true;
                    precursorStreamCnt++;
                    if (((Entry) entry).getRegion() != null) {
                        String regionId = ((Entry) entry).getRegion().getRegionId();
                        if (allIsTheSameRegion) {
                            if (tmpRegion == null) {
                                tmpRegion = regionId;
                            } else {
                                allIsTheSameRegion = regionId != null && regionId.equals(tmpRegion);
                            }
                        }
                    }
                }
            }
        }
        if (streamingInfo.isFoundStreamSuccessor()) {
            this.foundStreamPrecursor();
        }
        streamingInfo.setPreviousRegion(allIsTheSameRegion);
        previousIsFinished = !foundStreamPrecursor;

        // Detect successor
        for (IEntry entry : successorsEntries) {
            Class<? extends AbstractExecutor> executorClz = entry.getExecutorClz();
            if (AbstractStreamNodeExecutor.class.isAssignableFrom(executorClz)) {
                foundStreamSuccessor = true;
                successorStreamCnt++;
            }
        }
        if (!foundStreamSuccessor) {
            this.notFoundStreamSuccessor();
        }

        // To controller that successors execute process after current node had finished.
        finallyLatch = successorStreamCnt == 0 ? new CountDownLatch(precursorStreamCnt) : null;
        // The waiting times of last node which is streaming is related to all precursor count for streaming processor.
        streamLatch = new CountDownLatch(precursorStreamCnt);

        streamingInfo.setPrecursorStreamCnt(precursorStreamCnt);
        streamingInfo.setSuccessorStreamCnt(successorStreamCnt);
        streamingInfo.setFoundStreamPrecursor(foundStreamPrecursor);
        streamingInfo.setFoundStreamSuccessor(foundStreamSuccessor);

        // According to de precursor-successor relationship obtain above. All message queue and exchange are initialized
        // to initialize the push-flow operation in the subsequent life cycle functions.
        this.prepareDataQueue();
        return super.onInit() && onChildInit();
    }

    /**
     * 提供子类初始化重写接口
     *
     * @return 默认返回true，留给子类重写实现
     */
    protected boolean onChildInit() throws ExecuteException {
        return true;
    }

    /**
     * 准备转运数据队列
     *
     * @throws ExecuteException -
     */
    private void prepareDataQueue() throws ExecuteException {
        altassChannel.open();
        streamingInfo.setDistributePrevious(((Entry) this.entry).getRegion() != null);

        int count = 0;
        // Build data queue of precursor node.
        if (streamingInfo.isFoundStreamPrecursor()) {
            for (IEntry precursorEntry : streamingInfo.getPrecursorEntries()) {
                // Need to reverse-listening data queue if precursor contains streaming node.
                if (!AbstractStreamNodeExecutor.class.isAssignableFrom(precursorEntry.getExecutorClz())) {
                    continue;
                }
                String precursorEntryNodeId = precursorEntry.getNodeId();
                altassChannel.connectPrevious(precursorEntryNodeId);
                count++;
            }
        }
        EXECUTOR_LOGGER("msg", "finished to initialize precursor data queues.", "count", count);

        count = 0;

        // Build successor's data queue.
        if (streamingInfo.isFoundStreamSuccessor()) {
            this.checkWhetherSuccessorDistribute();

            for (IEntry successorsEntry : streamingInfo.getSuccessorsEntries()) {
                if (!AbstractStreamNodeExecutor.class.isAssignableFrom(successorsEntry.getExecutorClz())) {
                    continue;
                }

                // Cache index and item for data split.
                streamingInfo.getStreamSuccessorIdxMap().put(count, successorsEntry);

                String successorsEntryNodeId = successorsEntry.getNodeId();
                altassChannel.connectSuccessor(successorsEntryNodeId);
                count++;
            }
        }
        EXECUTOR_LOGGER("msg", "finished to initialize successor data queues.", "count", count);

    }

    /**
     * Check whether the successor node satisfies the distributed condition.
     */
    private void checkWhetherSuccessorDistribute() {
        String tmpRegionId = null;
        /*
         * 满足以下条件的，均不符合分布式的条件
         * 1. 直接后继中存在非流式节点
         * 2. 直接后继中存在非组节点
         * 3. 直接后继中存在regionId与其他节点不一致
         */
        for (IEntry successorsEntry : streamingInfo.getSuccessorsEntries()) {
            if (!AbstractStreamNodeExecutor.class.isAssignableFrom(successorsEntry.getExecutorClz())) {
                streamingInfo.setDistributeNext(false);
                break;
            }
            ARegion region = ((Entry) successorsEntry).getRegion();

            if (region == null) {
                streamingInfo.setDistributeNext(false);
                break;
            } else {
                String currentRegionId = region.getRegionId();
                if (tmpRegionId == null) {
                    tmpRegionId = currentRegionId;
                } else if (!tmpRegionId.equals(currentRegionId)) {
                    streamingInfo.setDistributeNext(false);
                    break;
                }
            }
        }
    }

    /**
     * 没有发现直接后继节点包含有流式处理特性的节点
     */
    protected void notFoundStreamSuccessor() throws ExecuteException {
        // 默认空实现
    }

    /**
     * 发现直接前驱节点包含有流式处理特性的节点
     */
    protected void foundStreamPrecursor() throws ExecuteException {
        // 默认空实现
    }

    @Override
    public void pushData(StreamData streamData) throws ExecuteException {
        streamData.setEvent(groupMode.get());
        streamData.setGroupKey(groupKey.get());
        altassChannel.publish(streamData);
        streamingInfo.incrCount();
    }


    /**
     * 否则将执行普通的节点处理，onNodeProcessing 已经被当前抽象实现，因此，替代的实现接口为
     * onNodeNormalProcessing
     *
     * @return -
     * @throws ExecuteException -
     * @see IStream#onNodeNormalProcessing()
     * @see IStream#isStreamingProcessing()
     */
    @Override
    public final boolean onNodeProcessing() throws ExecuteException {
        // 判断是否是流式处理节点，并且消费对象不存在
        if (isStreamingProcessing() && ((AltassRabbitChannel) altassChannel).getConsumer() != null) {
            // 当前节点是流式处理节点，但是如果没有发现后继流式节点，那么走阻塞性的流式处理，
            // 后继没有管道，那么当前节点一定是数据落地节点
            if (!streamingInfo.isFoundStreamSuccessor()) {
                EXECUTOR_LOGGER("Streaming processing but had degenerated, and successor will be waiting for current node finish.");
                try {
                    this.streamCoreProcessor();
                } catch (IOException | InterruptedException e) {
                    throw new ExecuteException(e);
                }

            } else {
                EXECUTOR_LOGGER("Streaming processing.");
                int delay = calculateStreamLayer() * START_DELAY;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            streamCoreProcessor();
                        } catch (IOException | InterruptedException | ExecuteException e) {
                            e.printStackTrace();
                        }
                    }
                }, delay);

            }
            return true;

        } else {
            EXECUTOR_LOGGER("Streaming Executor but had not use streaming process way.");
            // 如果不使用流式处理，那么继续走常规处理
            return this.onNodeNormalProcessing();
        }
    }

    /**
     * 处理流核心方法
     * <p>
     * 注意，不管是否是后接管道流式节点，只要当前是流式处理，那么就会执行该方法
     */
    @SuppressWarnings("deprecation")
    private void streamCoreProcessor() throws IOException, InterruptedException, ExecuteException {
        altassChannel.publish(new StreamData(currentNodeId, StreamEvent.EVENT_START));
        Thread.sleep(200);

        while (true) {
            byte[] body = altassChannel.receive();
            StreamData coveredData = transformData(body);

            String dataSrc;
            if (coveredData != null) {
//                            ITransaction transaction = altassChannel.getTransaction();
//                            transaction.setTransactionId(coveredData.getUniqueId());
//                            transaction.begin();
//                            transaction.commit();
                dataSrc = coveredData.getStreamSrc();
            } else {
                continue;
            }

            if (coveredData.getHead().equalsIgnoreCase("EVENT")) {
                // 事件处理
                if (StreamEvent.EVENT_START == coveredData.getEvent()) {
                    onStreamOpen(coveredData);                                         // 流打开事件
                } else if (StreamEvent.EVENT_FINISHED == coveredData.getEvent()) {
                    onStreamClose(coveredData);                                             // 流关闭事件
                    /*
                     * 注意，此处释放的条件 count > 1 的原因是管道流的传输结束前，前驱数据流的推送输出能力远远高于当前节
                     * 点的处理消费能力，也就是数据流可能早已推送完成并已进入处理队列，流关闭事件较当前节点一般是提前量的
                     * 为了防止所有的直接前驱节点的事件全部完成导致当前节点释放stream（但事实上当前节点并没有消费完所有的
                     * 数据），因此追加 count > 1 判定释放，最后一个将在while循环体完全结束后释放
                     */

                    if (streamLatch.getCount() > 1 && streamLatch != null) {
                        streamLatch.countDown();
                    }
                    // 跳出之前需要把对应的数据队列需要手动删除
                    altassChannel.close(dataSrc);
                    if (streamingInfo.isPreviousRegion() && finallyLatch != null) {
                        long count = finallyLatch.getCount();
                        for (int i = 0; i < count - 1; i++) {
                            finallyLatch.countDown();
                        }
                    }
                    previousIsFinished = true;
                    postFinished();
                    break;
                } else if (StreamEvent.EVENT_SKIP == coveredData.getEvent()) {
                } else if (StreamEvent.EVENT_GROUP_FINISHED == coveredData.getEvent()) {
                    this.onGroupFinish(coveredData);
                }

            } else if (coveredData.getHead().equalsIgnoreCase("DATA")) {

                if (coveredData.getEvent() == StreamEvent.EVENT_GROUP_DATA) {
                    groupMode.set(StreamEvent.EVENT_GROUP_DATA);
                    groupKey.set(coveredData.getGroupKey());
                } else {
                    groupMode.set(StreamEvent.EVENT_DATA);
                }
                // = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = *
                // = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = *

                // 处理流式数据核心方法，由子类负责实现处理逻辑，注意如果处理正常就必须返回一个非 null 的流数据对象
                // 实例，以表示处理成功，后续流程将处理成功后的数据通过管道继续往后传递（如果当前节点的后继节点包含
                // 有流式处理节点）
                this.onStreamProcessing(coveredData);

                // = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = *
                // = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = *

            }
        }

        this.onCurrentProcessFinished();
        Thread.sleep(200);
        // 释放最后一个 streamLatch
        if (streamLatch != null && streamLatch.getCount() > 0) {
            streamLatch.countDown();
        }
        // 释放 finallyLatch，允许直接后继启动运行
        if (finallyLatch != null) {
            finallyLatch.countDown();
        }
    }

    protected void onGroupFinish(StreamData coveredData) throws ExecuteException {
        groupMode.set(StreamEvent.EVENT_GROUP_FINISHED);
        groupKey.set(coveredData.getGroupKey());
        this.pushData(coveredData);
    }

    protected void postFinished() throws ExecuteException {
        if (previousIsFinished) {
            if (streamingInfo.isDistributeNext()) {
                for (IEntry entry : streamingInfo.getStreamSuccessorIdxMap().values()) {
                    Integer cnt = streamingInfo.getPushDataCntMap().get(entry.getNodeId());
                    altassChannel.publish(new StreamData(currentNodeId, StreamEvent.EVENT_FINISHED, cnt));
                }
            } else {
                altassChannel.publish(new StreamData(currentNodeId, StreamEvent.EVENT_FINISHED, streamingInfo.getDataPushCount()));
            }
        }
    }

    protected void onCurrentProcessFinished() throws ExecuteException {

    }

    @Override
    public void onStreamOpen(StreamData data) throws ExecuteException {

    }

    @Override
    public void onStreamClose(StreamData data) throws ExecuteException {

    }

    @Override
    public boolean isStreamingProcessing() throws ExecuteException {
        return true;
    }

    @Override
    protected boolean onDisconnect() {
        throw new UnsupportedOperationException();
    }

    /**
     * 计算直接后继的流式节点深度
     *
     * @return 返回当前节点后继流式节点的深度
     */
    protected int calculateStreamLayer() {
        return calculateStreamLayer(null);
    }

    /**
     * 计算直接后继的流式节点深度，递归计算
     *
     * @param entry 需要计算的节点元素
     * @return 当前节点后继流式节点的深度
     */
    private int calculateStreamLayer(IEntry entry) {
        List<IEntry> successorsEntries;
        if (entry == null) {
            entry = this.entry;
        }
        successorsEntries = getCurrentJob().getEntries(entry.obtainSuccessors());

        int max = 1;
        // 计算当前节点后（包含当前流式节点），有多少层流式处理层级
        for (IEntry successorsEntry : successorsEntries) {
            if (AbstractStreamNodeExecutor.class.isAssignableFrom(successorsEntry.getExecutorClz())) {
                // 包含，继续计算下一层级
                int successorDeep = calculateStreamLayer(successorsEntry);
                max = successorDeep > max ? successorDeep : max;
            }
        }
        return max + 1;
    }

    @Override
    public void onRollback(String transactionId) {
        // TODO: 事务回滚
    }

    @Override
    public void onInterrupted(AbstractTransaction.Status status) {
        // TODO：事务中断
    }

    /**
     * 内部简单解析byte[] 为StreamData对象
     *
     * @param data
     * @return
     */
    protected StreamData transformData(byte[] data) {
        try {
            String dataStr = new String(data, "UTF-8");
            return JSON.parseObject(dataStr, StreamData.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
