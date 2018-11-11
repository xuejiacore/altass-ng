package org.chim.altass.core.streaming;

/**
 * Class Name: RollbackListener
 * Create Date: 11/3/18 11:33 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface TransactionListener {

    void onRollback(String transactionId);

    void onInterrupted(AbstractTransaction.Status status);


}
