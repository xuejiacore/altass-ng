package org.chim.altass.core.exception;

/**
 * Class Name: BrokenExecutorException
 * Create Date: 2017/9/2 1:31
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class BrokenExecutorException extends ExecuteException {
    private static final long serialVersionUID = 8532429048382672718L;

    public BrokenExecutorException() {
    }

    public BrokenExecutorException(String message) {
        super(message);
    }

    public BrokenExecutorException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrokenExecutorException(Throwable cause) {
        super(cause);
    }
}
