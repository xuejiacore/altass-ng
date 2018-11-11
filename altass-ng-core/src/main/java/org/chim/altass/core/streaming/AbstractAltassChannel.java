package org.chim.altass.core.streaming;

import org.chim.altass.core.constant.StreamData;

/**
 * Class Name: AbstractDataTransformer
 * Create Date: 11/3/18 5:23 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public abstract class AbstractAltassChannel implements IAltassChannel, IRouting {

    private ITransaction transaction = null;

    protected StreamingInfo streamingInfo = null;

    public AbstractAltassChannel() {
    }


    public AbstractAltassChannel(StreamingInfo streamingInfo, ITransaction transaction) {
        this.streamingInfo = streamingInfo;
        this.transaction = transaction;
    }

    @Override
    public void publish(StreamData data) {
        if (transaction == null) {
            // 非事务提交
            doPublish(data);
        } else {
            transaction.setTransactionId(data.getUniqueId());
            // 开启事务提交
            transaction.begin();
            // TODO: 传输数据
            doPublish(data);

            try {
                transaction.commit();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                transaction.close();
            }
        }
    }

    @Override
    public byte[] receive() {

        return doReceive();
    }

    @Override
    public ITransaction getTransaction() {
        return transaction;
    }

    @Override
    public void close() {
        if (transaction != null) {
            transaction.close();
        }
        onChannelClose();
    }

    @Override
    public void close(String tag) {

    }

    protected abstract void onChannelClose();

    public abstract boolean doPublish(StreamData data);

    public abstract byte[] doReceive();
}
