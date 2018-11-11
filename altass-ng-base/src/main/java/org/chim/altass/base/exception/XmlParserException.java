/**
 * Project: x-framework
 * Package Name: org.ike.core.exception
 * Author: Xuejia
 * Date Time: 2016/12/10 18:43
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.exception;

/**
 * Class Name: XmlParserException
 * Create Date: 2016/12/10 18:43
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class XmlParserException extends Exception {
    public XmlParserException() {
    }

    public XmlParserException(String message) {
        super(message);
    }

    public XmlParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlParserException(Throwable cause) {
        super(cause);
    }
}
