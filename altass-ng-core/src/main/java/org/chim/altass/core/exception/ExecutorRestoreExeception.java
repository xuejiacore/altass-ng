package org.chim.altass.core.exception;

/**
 * Class Name: ExecutorRestoreExeception
 * Create Date: 2017/10/20 1:55
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class ExecutorRestoreExeception extends ExecuteException {

    private static final long serialVersionUID = 5570654147331146272L;

    public ExecutorRestoreExeception() {
    }

    public ExecutorRestoreExeception(String message) {
        super(message);
    }

    public ExecutorRestoreExeception(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutorRestoreExeception(Throwable cause) {
        super(cause);
    }

    public ExecutorRestoreExeception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
