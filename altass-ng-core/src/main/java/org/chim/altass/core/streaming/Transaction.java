package org.chim.altass.core.streaming;

import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.toolkit.RedissonToolkit;
import org.redisson.api.RBucket;
import org.redisson.api.RCountDownLatch;

import javax.transaction.RollbackException;
import java.util.concurrent.TimeUnit;

/**
 * Class Name: RTransaction
 * Create Date: 11/3/18 8:01 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class Transaction extends AbstractTransaction {

    private RCountDownLatch transactionLock = null;
    private boolean isLocker = true;
    private static final EurekaSystemRedisKey ROLLBACK_KEY = EurekaSystemRedisKey.ALTASS$STREAM_TRANSACTION_ROLLBACK;
    private static final EurekaSystemRedisKey TRANSACTION_KEY = EurekaSystemRedisKey.ALTASS$STREAM_TRANSACTION;

    public Transaction() {

    }

    public Transaction(String transactionID) {
        this.transactionID = transactionID;
    }

    public Transaction(TransactionListener listener) {
        this.rollbackListener = listener;
    }

    public Transaction(String transactionID, TransactionListener listener) {
        this.transactionID = transactionID;
        this.rollbackListener = listener;
    }

    @Override
    public void begin() {
        transactionLock = RedissonToolkit.getInstance().getCountDownLatch(TRANSACTION_KEY, transactionID);
        isLocker = transactionLock.getCount() == 0;
        if (isLocker) {
            transactionLock.trySetCount(1);
        }
    }

    @Override
    public void transactionCommit() throws RollbackException, InterruptedException {
        if (isLocker) {
            // 不设置回滚标记释放
            if (timeout != null && timeout > 0) {
//                transactionLock.await(timeout, TimeUnit.MILLISECONDS);
            } else {
//                transactionLock.await();
            }
            Object bucket = RedissonToolkit.getInstance().getBucket(ROLLBACK_KEY, transactionID);
            if (bucket != null) {
                String val = String.valueOf(bucket);
                Status status = Status.TIMEOUT;
                try {
                    status = Status.valueOf(val);
                } catch (Throwable ignored) {
                }
                switch (status) {
                    case ROLLBACK:
                        // 是回滚操作
                        throw new RollbackException();
                }
            } else {
                throw new InterruptedException("Timeout");
            }
        } else {
            transactionLock.countDown();
        }
    }

    @Override
    public void rollback(Object data) {
        // 设置回滚标记
        RedissonToolkit.getInstance().setBucket(ROLLBACK_KEY, transactionID, Status.ROLLBACK);
        // 回滚
        transactionLock.countDown();
    }

    @Override
    public void close() {
        transactionLock.delete();
    }

    public static void main(String[] args) {
        Transaction tx = new Transaction("test", new TransactionListener() {
            @Override
            public void onRollback(String transactionId) {
                RBucket rBucket = RedissonToolkit.getInstance().getRBucket(ROLLBACK_KEY, transactionId);
                System.out.println("回滚操作:" + rBucket.get());
                rBucket.delete();
            }

            @Override
            public void onInterrupted(Status status) {
                System.out.println("中断:" + status.name());
            }
        });
        tx.setTimeout(6000L);
        int testData = 0;
        try {
            tx.begin();
            System.out.println("Do Something...");
            tx.commit();
            System.out.println("提交了");
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(testData);
        } finally {
            tx.close();
        }
        System.out.println("结束");
    }
}
