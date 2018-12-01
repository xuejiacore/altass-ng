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
    private byte event = StreamEvent.EVENT_DATA;                // transfer message
    private String groupKey = null;
    private Integer count = 0;                                  // unused
    private String head = null;                                 // data header
    private byte dataType = DataStructure.JSON;                 // data transfer type

    public StreamData() {
        this.uniqueId = UUID.randomUUID().toString();
    }

    public StreamData(String src) {
        this();
        this.streamSrc = src;
    }

    public StreamData(String src, byte event) {
        this();
        this.streamSrc = src;
        this.event = event;
    }

    public StreamData(String src, byte streamEvent, Object data) {
        this.event = streamEvent;
        this.streamSrc = src;
        this.data = data;
        this.uniqueId = UUID.randomUUID().toString();
    }

    public StreamData(String src, Object data) {
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

    public byte getEvent() {
        return event;
    }

    public void setEvent(byte event) {
        this.event = event;
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

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public byte getDataType() {
        return dataType;
    }

    /**
     * specify data field's type
     *
     * @param dataType data Type {@link DataStructure}
     * @see DataStructure
     */
    public void setDataType(byte dataType) {
        this.dataType = dataType;
    }

    public boolean dtis(byte dt) {
        return this.dataType == dt;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }
}
