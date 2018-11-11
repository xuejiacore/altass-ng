package org.chim.altass.core.streaming;

import javax.transaction.RollbackException;

/**
 * Class Name: Transaction
 * Create Date: 11/1/18 9:05 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Provides the transaction boundary while accessing a channel.
 *
 * <pre>
 *     <code>
 *      Transaction tx = ch.getTransaction();
 *      try {
 *          tx.begin();
 *          ...
 *          //
 *          ...
 *          tx.commit();
 *      } catch (Exception ex) {
 *          tx.rollback();
 *      } finally {
 *          tx.close();
 *      }
 *      </code>
 * </pre>
 */
public interface ITransaction {

    enum TransactionState {
        Started, Committed, RolledBack, Closed
    }

    void setTransactionId(String transactionId);

    /**
     * <p>
     * Starts a transaction boundary for the current channel operation. If a
     * transaction is already in progress, this method will join that transaction
     * using reference counting.
     * </p>
     */
    void begin();

    void transactionCommit() throws RollbackException, InterruptedException;

    /**
     * <p>
     * Indicates that the transaction can be successfully committed. It is
     * required that a transaction be in progress when this method is invoked.
     * </p>
     */
    void commit() throws InterruptedException;

    /**
     * <p>
     * Indicates that the transaction can must be aborted, It is required
     * that a transaction be in progress when this method is invoked.
     * </p>
     */
    void rollback(Object data);

    /**
     * <p>
     * Ends a transaction boundary for the current channel operation, If a
     * transaction is already in progress, this method will join that transaction
     * using reference counting. The transaction is completed only if there are
     * no more reference left for this transaction.
     * </p>
     */
    void close();
}
