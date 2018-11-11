package org.chim.altass.core.constant;

import java.util.UUID;

/**
 * Class Name: StreamResult
 * Create Date: 2017/10/24 19:58
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * A Stream Data Structure to Wrap Streaming Data
 */
public class StreamData {

    private String uniqueId = null;
    private Boolean successful = false;                         // data transfer successful
    private String streamSrc = null;                            // tag the source of current streaming data
    private Object data = null;                                 // data object will be transfer to json structure
    private String msg = null;                                  // transfer message
    private Integer count = 0;                                  // unused
    private String head = null;                                 //

    public StreamData() {
        msg = "#data";
        this.uniqueId = UUID.randomUUID().toString();
    }

    public StreamData(String src) {
        this();
        this.streamSrc = src;
    }

    public StreamData(String src, String msg) {
        this.msg = msg;
        this.streamSrc = src;
        this.uniqueId = UUID.randomUUID().toString();
    }

    public StreamData(String src, String msg, Object data) {
        this.msg = msg == null ? "#data" : msg;
        this.streamSrc = src;
        this.data = data;
        this.uniqueId = UUID.randomUUID().toString();
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStreamSrc() {
        return streamSrc;
    }

    public void setStreamSrc(String streamSrc) {
        this.streamSrc = streamSrc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
