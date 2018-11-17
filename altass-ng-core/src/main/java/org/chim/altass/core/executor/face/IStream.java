package org.chim.altass.core.executor.face;


import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.exception.ExecuteException;

/**
 * Class Name: IStream
 * Create Date: 2017/10/24 14:41
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Streaming-ize Node Interface
 */
public interface IStream {

    /**
     * The event of onStreamProcessing open.
     *
     * @param data the data when onStreamProcessing is opened.
     * @throws ExecuteException Execute Exception
     */
    void onStreamOpen(StreamData data) throws ExecuteException;

    /**
     * The entrance of a streaming node executor to process onStreamProcessing data.
     * <p>
     * Tips:General, you could implement your program in this function and must return a not null StreamData instance to
     * tell current executor processed successful. The onStreamProcessing data will be rollback to last executor to re-execute if the
     * StreamData is null. And then, the fail data will be pushed again to current node when retry is available.
     * <p>
     * The processed onStreamProcessing data will be pushed to next streaming node if successor contains streaming node. Current node
     * will be blocked if successor haven't any onStreamProcessing node, it means that all of successors could be started only after current
     * executor finished.
     *
     * @param data a json structure data from last streaming node.
     * @throws ExecuteException Execute Exception
     */
    void onStreamProcessing(byte[] data) throws ExecuteException;

    /**
     * 如果isStreamProcessor判定不需要采用流式处理，那么该方法将会被执行，这是普通的执行方法，由于onNodeProcessing已经被
     * 抽象重写，故由改方法提供给子类接口
     *
     * @return 如果处理成功，返回值为true，否则返回值为false
     * @throws ExecuteException 执行异常
     */
    boolean onNodeNormalProcessing() throws ExecuteException;

    /**
     * 由子类实现判断是否采用流式处理方式
     * <p>
     * 一般而言，如果上一个节点有管道流的特性，那么当前节点就应该以流式来接收上一个节点的流式数据，前提是：当前节点本身
     * 支持流式的操作
     *
     * @return 如果是使用流式处理，那么onStreamNodeProcessing方法将不会执行，执行stream方法，反之不执行stream方法
     * @throws ExecuteException 执行异常
     */
    boolean isStreamingProcessing() throws ExecuteException;

    /**
     * 流式处理结束事件
     *
     * @param data 流关闭事件数据
     * @throws ExecuteException 执行异常
     */
    void onStreamClose(StreamData data) throws ExecuteException;

    /**
     * 如果处理失败，是否需要重试
     *
     * @return 如果需要重试，那么返回值为true，否则返回值为false
     * @throws ExecuteException 执行异常
     */
    boolean retryIfFail() throws ExecuteException;
}
