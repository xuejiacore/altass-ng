/**
 * Project: x-framework
 * Package Name: org.ike.core.exception
 * Author: Xuejia
 * Date Time: 2016/11/20 20:57
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.exception;

/**
 * Class Name: CoreException
 * Create Date: 2016/11/20 20:57
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class CoreException extends Exception {
    /**
     * 错误代码
     */
    private int errorCode = 0;

    public CoreException() {
    }

    public CoreException(String message) {
        super(message);
    }

    public CoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoreException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
