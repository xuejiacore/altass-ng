package org.chim.altass.core;


import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.exception.ExecuteException;

/**
 * Class Name: IPipeline
 * Create Date: 2017/10/24 17:04
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface IPipeline {

    /**
     * 管道数据推接口
     *
     * @param streamData 需要推到管道流中的数据
     */
    void pushData(StreamData streamData) throws ExecuteException;

    /**
     * 数据回滚
     * 由于在推数据后，后继节点可能执行异常，对于这种执行异常的数据，管道流的输入方必须保证处理了异常数据，如：将回滚数
     * 据重新发送
     *
     * @param data 流式数据
     */
    void rollback(StreamData data);
}
