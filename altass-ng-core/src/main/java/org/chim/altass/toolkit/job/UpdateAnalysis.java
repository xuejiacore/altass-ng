package org.chim.altass.toolkit.job;


import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.connector.Connector;

import java.util.List;

/**
 * Class Name: JobUpdator
 * Create Date: 18-3-13 下午8:29
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 作业的更新包分析结果实体
 */
@Elem(alias = "updateAnalysis")
public class UpdateAnalysis {

    @Elem(alias = "changedConnectors")
    private List<Connector> changedConnectors = null;           // 变更的连线
    @Elem(alias = "delConnectors")
    private List<Connector> delConnectors = null;               // 删除的连线
    @Elem(alias = "newConnectors")
    private List<Connector> newConnectors = null;               // 新增的连线
    @Elem(alias = "changedEntries")
    private List<IEntry> changedEntries = null;                 // 变更的节点
    @Elem(alias = "newEntries")
    private List<IEntry> newEntries = null;                     // 新增的节点
    @Elem(alias = "deleteEntries")
    private List<IEntry> deleteEntries = null;                  // 删除的节点
    @Elem(alias = "bfsSorted")
    private List<String> bfsSorted = null;                      // 广度优先搜索排序
    @Elem(alias = "dfsSorted")
    private List<String> dfsSorted = null;                      // 深度优先搜索排序
    @Attr(alias = "desc")
    private String desc = null;                                 // xml描述
    @Attr(alias = "jobId")
    private String jobId = null;                                // 影响的作业
    @Attr(alias = "bfsDepth")
    private Integer bfsDepth = null;                            // 广度优先搜索深度
    @Attr(alias = "dfsDepth")
    private Integer dfsDepth = null;                            // 深度优先搜素深度

    public UpdateAnalysis() {
    }

    public UpdateAnalysis(String jobId) {
        this.jobId = jobId;
        this.desc = "Job Update Package";
    }

    /**
     * 判断是否没有任何变更
     *
     * @return 如果没有任何变更，那么返回值为true，否则返回值为false
     */
    public boolean nothingChanged() {
        return changedConnectors == null &&
                newConnectors == null &&
                delConnectors == null &&
                changedEntries == null &&
                newEntries == null &&
                deleteEntries == null;
    }

    public List<Connector> getChangedConnectors() {
        return changedConnectors;
    }

    public void setChangedConnectors(List<Connector> changedConnectors) {
        this.changedConnectors = changedConnectors;
    }

    public List<Connector> getDelConnectors() {
        return delConnectors;
    }

    public void setDelConnectors(List<Connector> delConnectors) {
        this.delConnectors = delConnectors;
    }

    public List<Connector> getNewConnectors() {
        return newConnectors;
    }

    public void setNewConnectors(List<Connector> newConnectors) {
        this.newConnectors = newConnectors;
    }

    public List<IEntry> getChangedEntries() {
        return changedEntries;
    }

    public void setChangedEntries(List<IEntry> changedEntries) {
        this.changedEntries = changedEntries;
    }

    public List<IEntry> getNewEntries() {
        return newEntries;
    }

    public void setNewEntries(List<IEntry> newEntries) {
        this.newEntries = newEntries;
    }

    public List<IEntry> getDeleteEntries() {
        return deleteEntries;
    }

    public void setDeleteEntries(List<IEntry> deleteEntries) {
        this.deleteEntries = deleteEntries;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Integer getBfsDepth() {
        return bfsDepth;
    }

    public void setBfsDepth(Integer bfsDepth) {
        this.bfsDepth = bfsDepth;
    }

    public Integer getDfsDepth() {
        return dfsDepth;
    }

    public void setDfsDepth(Integer dfsDepth) {
        this.dfsDepth = dfsDepth;
    }

    public List<String> getBfsSorted() {
        return bfsSorted;
    }

    public void setBfsSorted(List<String> bfsSorted) {
        this.bfsSorted = bfsSorted;
    }

    public List<String> getDfsSorted() {
        return dfsSorted;
    }

    public void setDfsSorted(List<String> dfsSorted) {
        this.dfsSorted = dfsSorted;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
