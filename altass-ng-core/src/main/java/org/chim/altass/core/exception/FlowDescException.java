/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.exception
 * Author: Xuejia
 * Date Time: 2016/12/27 10:28
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.exception;

/**
 * Class Name: FlowDescException
 * Create Date: 2016/12/27 10:28
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class FlowDescException extends RuntimeException {
    private static final long serialVersionUID = -2034387622693498048L;

    public FlowDescException() {
    }

    public FlowDescException(String message) {
        super(message);
    }

    public FlowDescException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlowDescException(Throwable cause) {
        super(cause);
    }
}
