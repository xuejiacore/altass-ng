package org.chim.altass.core.streaming;

import javax.transaction.RollbackException;

/**
 * Class Name: AbstractTransaction
 * Create Date: 11/3/18 8:07 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public abstract class AbstractTransaction implements ITransaction {

    public enum Status {
        COMMITTED, ROLLBACK, TIMEOUT, UNKNOWN
    }

    String transactionID = null;
    TransactionListener rollbackListener = null;

    protected Long timeout = 30000L;                      // timeout ms

    @Override
    public void commit() throws InterruptedException {
        try {
            transactionCommit();
        } catch (RollbackException e) {
            if (rollbackListener != null) {
                rollbackListener.onRollback(transactionID);
            } else {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            if (rollbackListener != null) {
                String message = e.getMessage();
                rollbackListener.onInterrupted(message.equalsIgnoreCase(Status.TIMEOUT.name()) ? Status.TIMEOUT : Status.UNKNOWN);
            } else {
                throw e;
            }
        }
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void setTransactionId(String transactionId) {
        this.transactionID = transactionId;
    }
}
