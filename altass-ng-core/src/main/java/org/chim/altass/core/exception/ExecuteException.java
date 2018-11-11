/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.exception
 * Author: Xuejia
 * Date Time: 2016/12/16 12:05
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.exception;

/**
 * Class Name: ExecuteException
 * Create Date: 2016/12/16 12:05
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 执行异常基类
 * <p>
 * 所有的节点运行时异常的基础类，从线程开始到执行完成
 */
public class ExecuteException extends Exception {

    private static final long serialVersionUID = 860825390727284559L;

    public ExecuteException() {
        super();
    }

    public ExecuteException(String message) {
        super(message);
    }

    public ExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecuteException(Throwable cause) {
        super(cause);
    }

    protected ExecuteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return (message != null) ? (s + " FROM 【" + Thread.currentThread().getName() + "】: " + message) : s;
    }
}
