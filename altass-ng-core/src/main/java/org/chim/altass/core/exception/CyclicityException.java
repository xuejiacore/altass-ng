package org.chim.altass.core.exception;

import java.util.List;

/**
 * Class Name: CyclicityException
 * Create Date: 2017/10/20 0:21
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 抛出有向环的异常信息
 */
public class CyclicityException extends FlowDescException {
    private static final long serialVersionUID = -7696751273507775715L;

    private List<String> troubleNode = null;

    public CyclicityException() {
    }

    public CyclicityException(String message) {
        super(message);
    }

    public CyclicityException(String message, Throwable cause) {
        super(message, cause);
    }

    public CyclicityException(Throwable cause) {
        super(cause);
    }

    public void setTroubleNode(List<String> troubleNode) {
        this.troubleNode = troubleNode;
    }

    public List<String> getTroubleNode() {
        return troubleNode;
    }
}
