package org.chim.altass.base.parser.exml.exception;

/**
 * Class Name: XmlException
 * Create Date: 17-12-11 下午12:42
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class XmlException extends Exception{
    private static final long serialVersionUID = 1085632786367241985L;

    public XmlException() {
        super();
    }

    public XmlException(String message) {
        super(message);
    }

    public XmlException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlException(Throwable cause) {
        super(cause);
    }
}
