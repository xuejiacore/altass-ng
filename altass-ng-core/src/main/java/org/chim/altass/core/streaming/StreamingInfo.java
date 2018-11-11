package org.chim.altass.core.streaming;

import org.chim.altass.core.domain.IEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Name: StreamingBean
 * Create Date: 11/4/18 3:38 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class StreamingInfo {
    IEntry entry = null;                                              // 与当前执行器绑定的节点元素
    List<IEntry> precursorEntries = null;                  // The entries list of precursor.
    List<IEntry> successorsEntries = null;                 // The entries list of successor.
    Map<Integer, IEntry> streamSuccessorIdxMap = null;     // The index mapper of streaming successor.
    Map<String, Integer> pushDataCntMap = null;            // The pushed data count.
    int precursorStreamCnt = 0;                            // Count of precursor streaming node.
    int successorStreamCnt = 0;                            // Count of successor streaming node.
    boolean foundStreamPrecursor = false;                  // Detected precursor streaming node existed.
    boolean foundStreamSuccessor = false;                  // Detected successor streaming node existed.
    boolean isDistributeNext = true;
    boolean isPreviousRegion = false;                      // 前驱是一个分布式组
    protected boolean isDistributePrevious = true;                   //
    /**
     * 数据推流数目，用于后继节点校验数据完整性
     */
    int dataPushCount = 0;

    public StreamingInfo() {
        streamSuccessorIdxMap = new HashMap<>();
        pushDataCntMap = new HashMap<>();
    }

    public List<IEntry> getPrecursorEntries() {
        return precursorEntries;
    }

    public void setPrecursorEntries(List<IEntry> precursorEntries) {
        this.precursorEntries = precursorEntries;
    }

    public List<IEntry> getSuccessorsEntries() {
        return successorsEntries;
    }

    public void setSuccessorsEntries(List<IEntry> successorsEntries) {
        this.successorsEntries = successorsEntries;
    }

    public Map<Integer, IEntry> getStreamSuccessorIdxMap() {
        return streamSuccessorIdxMap;
    }

    public void setStreamSuccessorIdxMap(Map<Integer, IEntry> streamSuccessorIdxMap) {
        this.streamSuccessorIdxMap = streamSuccessorIdxMap;
    }

    public Map<String, Integer> getPushDataCntMap() {
        return pushDataCntMap;
    }

    public void setPushDataCntMap(Map<String, Integer> pushDataCntMap) {
        this.pushDataCntMap = pushDataCntMap;
    }

    public int getPrecursorStreamCnt() {
        return precursorStreamCnt;
    }

    public void setPrecursorStreamCnt(int precursorStreamCnt) {
        this.precursorStreamCnt = precursorStreamCnt;
    }

    public int getSuccessorStreamCnt() {
        return successorStreamCnt;
    }

    public void setSuccessorStreamCnt(int successorStreamCnt) {
        this.successorStreamCnt = successorStreamCnt;
    }

    public IEntry getEntry() {
        return entry;
    }

    public void setEntry(IEntry entry) {
        this.entry = entry;
    }

    public boolean isFoundStreamPrecursor() {
        return foundStreamPrecursor;
    }

    public void setFoundStreamPrecursor(boolean foundStreamPrecursor) {
        this.foundStreamPrecursor = foundStreamPrecursor;
    }

    public boolean isFoundStreamSuccessor() {
        return foundStreamSuccessor;
    }

    public void setFoundStreamSuccessor(boolean foundStreamSuccessor) {
        this.foundStreamSuccessor = foundStreamSuccessor;
    }

    public boolean isDistributeNext() {
        return isDistributeNext;
    }

    public void setDistributeNext(boolean distributeNext) {
        isDistributeNext = distributeNext;
    }

    public boolean isPreviousRegion() {
        return isPreviousRegion;
    }

    public void setPreviousRegion(boolean previousRegion) {
        isPreviousRegion = previousRegion;
    }

    public boolean isDistributePrevious() {
        return isDistributePrevious;
    }

    public void setDistributePrevious(boolean distributePrevious) {
        isDistributePrevious = distributePrevious;
    }

    public synchronized int getDataPushCount() {
        return dataPushCount;
    }

    public void setDataPushCount(int dataPushCount) {
        this.dataPushCount = dataPushCount;
    }

    public synchronized int incrCount() {
        return ++dataPushCount;
    }
}
