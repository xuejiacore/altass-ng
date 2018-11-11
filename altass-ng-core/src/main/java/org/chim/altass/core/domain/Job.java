/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.domain
 * Author: Xuejia
 * Date Time: 2016/12/19 11:43
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain;

import org.chim.altass.base.parser.exml.Meta;
import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.core.domain.buildin.entry.EndEntry;
import org.chim.altass.core.domain.buildin.entry.Entries;
import org.chim.altass.core.domain.connector.Connector;
import org.chim.altass.core.domain.connector.Connectors;
import org.chim.altass.core.domain.buildin.attr.ARegion;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.domain.buildin.entry.StartEntry;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.general.JobExecutor;
import org.chim.altass.toolkit.RedissonToolkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.chim.altass.core.constant.ExecutorAttr.ATTR_IS_JOB;

/**
 * Class Name: Job
 * Create Date: 2016/12/19 11:43
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 所有作业的基类
 */
@Elem(alias = "job", interfacing = true)
public class Job extends Entry implements Cloneable {

    private static final long serialVersionUID = 4196900076442468064L;

    {
        nodeType = EntryType.Job.TYPE_JOB;
    }

    /**
     * 当前作业的所有可执行元素
     * 该映射的作用是用于一个作业中快速根据节点的ID查找节点
     */
    private Map<String, IEntry> entryMap = new HashMap<>();

    @Attr(alias = "jobId")
    private String jobId = null;

    /**
     * 开始节点
     */
    @Elem(alias = "startNode")
    private StartEntry startNode = null;

    /**
     * 结束节点
     */
    @Elem(alias = "endNode")
    private EndEntry endNode = null;

    /**
     * 节点集合（除开始和结束节点）
     */
    @Elem(alias = "entries", priority = 0)
    private Entries entries = null;

    /**
     * 节点的连线集合
     */
    @Elem(alias = "connectors")
    private Connectors connectors = null;

    /**
     * 作业节点的阻塞性
     */
    private Boolean isObstructive = true;

    /**
     * 作业的后继节点，通常出现在子作业中
     */
    private List<IEntry> successors = null;

    /**
     * 当前子作业的父作业id
     */
    private String parentJobId = null;

    private RedissonToolkit redissonToolkit = RedissonToolkit.getInstance();

    /**
     * 创建作业
     */
    public Job() {
        new StartEntry(this, 300, 400);
        new EndEntry(this, 300, 650);
        setNodeId(getNodeId());
        setExecutorClz(JobExecutor.class);
    }

    /**
     * 使用给定的开始节点以及结束节点id（只做调试使用）
     *
     * @param startNodeId 开始节点ID
     * @param endNodeId   结束节点ID
     */
    public Job(String startNodeId, String endNodeId) {
        new StartEntry(startNodeId, this, 300, 400);
        new EndEntry(endNodeId, this, 300, 400);
        setNodeId(getNodeId());
        setExecutorClz(JobExecutor.class);
    }

    /**
     * 获得作业的开始节点
     *
     * @return 作业的开始节点
     */
    public Node obtainStartNode() {
        return this.startNode;
    }

    /**
     * 获得作业的结束节点
     *
     * @return 作业的结束节点
     */
    public Node obtainEndNode() {
        return this.endNode;
    }

    public Entry getStartNode() {
        return startNode;
    }

    public void setStartNode(StartEntry startNode) {
        this.startNode = startNode;
        this.entryMap.put(this.startNode.getNodeId(), this.startNode);
    }

    public Entry getEndNode() {
        return endNode;
    }

    public void setEndNode(EndEntry endNode) {
        this.endNode = endNode;
        this.entryMap.put(this.endNode.getNodeId(), this.endNode);
    }

    public Entries getEntries() {
        return entries;
    }

    /**
     * 设置作业的执行元素
     * <p>
     * 在设置完作业的执行元素之后，需要重新初始化当前映射关系，首先将开始结束两个特殊节点加入，然后对所有设置进的元素逐一
     * 添加到映射关系中。
     * <p>
     * 映射关系的存在主要是为了能够方便作业内节点元素的查找操作
     *
     * @param entries 需要设置的元素
     */
    public void setEntries(Entries entries) {
        this.entryMap.clear();
        this.entries = entries;
        entryMap.put(startNode.getNodeId(), startNode);
        entryMap.put(endNode.getNodeId(), endNode);
        this.entries.getEntries().forEach(entry -> this.entryMap.put(entry.getNodeId(), entry));
    }

    /**
     * 添加一个元素
     *
     * @param entry 需要添加的元素
     * @return 元素如果添加成功，那么返回值为true，否则返回值为false
     */
    public boolean addEntry(Entry entry) {
        if (this.entries == null) {
            this.entries = new Entries();
        }

        if (entry == null) {
            throw new FlowDescException("Entry could not be null.");
        }
        if (entry.getExecutorClz() == null) {
            throw new FlowDescException("Entry's executor could not be null.");
        }

        if (entry.getExecutorClz().isAssignableFrom(JobExecutor.class)) {
            entry.setChild(true);
        }

        this.entryMap.put(entry.getNodeId(), entry);
        return !this.entries.getEntries().contains(entry) && this.entries.addEntry(entry);
    }

    /**
     * 移除一个元素
     * 从一个作业中移除一个元素的时候，需要同时将与之关联的所有连线（不管是出度还是入度）一并移除
     *
     * @param entry 需要移除的元素
     * @return 如果移除成功，那么返回值为true，否则返回值为false
     */
    public boolean removeEntry(IEntry entry) {
        if (entry == null) {
            return false;
        }
        if (this.entries.removeEntry(entry)) {
            this.entryMap.remove(entry.getNodeId());
            // 移除了节点之后，需要移除所有与之关联的连线
            final String finalNodeId = entry.getNodeId();
            if (finalNodeId != null) {
                List<Connector> rmConnList = new ArrayList<>();

                // 删除的节点可能没有连线
                if (connectors != null && connectors.getConnectors() != null) {
                    connectors.getConnectors().stream()
                            .filter(connector -> connector != null &&
                                    (finalNodeId.equals(connector.getSourceId()) || finalNodeId.equals(connector.getTargetId())))
                            .forEach(rmConnList::add);
                    for (Connector connector : rmConnList) {
                        connectors.getConnectors().remove(connector);
                    }
                    List<IEntry> precursorEntries = getEntries(entry.obtainPrecursors());
                    for (IEntry precursorEntry : precursorEntries) {
                        // 删除前驱的出度
                        for (Connector connector : rmConnList) {
                            precursorEntry.delOutDegree(connector);
                        }
                    }
                    List<IEntry> successorEntries = getEntries(entry.obtainSuccessors());
                    for (IEntry successorEntry : successorEntries) {
                        // 删除后继的入度
                        for (Connector connector : rmConnList) {
                            successorEntry.delInDegree(connector);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 变更连线的链接类型
     *
     * @param srcEntry    连线的源
     * @param targetEntry 连线的目标
     * @param connectType 连线的类型
     * @return 如果连线的类型变更成功，那么返回值为true，否则返回值为false
     */
    public boolean changeConnectType(IEntry srcEntry, IEntry targetEntry, Integer connectType) {
        Connectors connectors = getConnectors();
        for (Connector connector : connectors.getConnectors()) {
            if (srcEntry.getNodeId().equals(connector.getSourceId())
                    && targetEntry.getNodeId().equals(connector.getTargetId())) {

                connector.setConnectType(connectType);

                // 同步更新目标节点的入度
                List<Connector> inDegree = getEntry(targetEntry.getNodeId()).getInDegree();
                for (Connector inConnector : inDegree) {
                    if (inConnector.getConnectorId().equals(connector.getConnectorId())) {
                        inConnector.setConnectType(connectType);
                    }
                }

                // 同步更新来源节点的出度
                List<Connector> outDegree = getEntry(srcEntry.getNodeId()).getOutDegree();
                for (Connector outConnector : outDegree) {
                    if (outConnector.getConnectorId().equals(connector.getConnectorId())) {
                        outConnector.setConnectType(connectType);
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 根据节点元素的ID获得节点元素属性
     *
     * @param entryId 节点的元素ID
     * @return 如果查找的元素ID存在，那么返回节点元素实例，否则返回值为null
     */
    public IEntry getEntry(String entryId) {
        return entryId == null ? null : this.entryMap.get(entryId);
    }

    /**
     * 根据连线id获得连线
     *
     * @param connectId 连线id
     * @return 连线
     */
    public Connector getConnector(String connectId) {
        if (connectId == null) {
            return null;
        }

        List<Connector> connectors = this.connectors.getConnectors();
        if (connectors == null || connectors.size() == 0) {
            return null;
        }
        for (Connector connector : connectors) {
            if (connectId.equals(connector.getConnectorId())) {
                return connector;
            }
        }
        return null;
    }

    /**
     * 根据节点元素ID的集合获得元素属性
     *
     * @param entriesId 节点元素的ID集合
     * @return 返回查找到的元素集合，如果为空，那么返回值为null
     */
    public List<IEntry> getEntries(List<String> entriesId) {
        List<IEntry> entries = null;
        if (entriesId != null) {
            entries = new ArrayList<>();
            for (String entryId : entriesId) {
                entries.add(this.entryMap.get(entryId));
            }
        }
        return entries;
    }

    /**
     * 根据元素的ID移除一个元素
     *
     * @param entryId 需要移除的元素ID
     * @return 如果移除成功，那么返回值为true，否则返回值为false
     */
    public boolean removeEntry(String entryId) {
        return removeEntry(this.entryMap.get(entryId));
    }

    public Connectors getConnectors() {
        return connectors;
    }

    /**
     * 设置当前JOB的所有连线
     *
     * @param connectors 连线
     */
    public void setConnectors(Connectors connectors) {
        this.connectors = connectors;
        // 如果调用这个方法，需要根据连线，刷新以便当前所有节点的连接情况
//        this.connectors.getConnectors().forEach(connector -> {
//            String sourceId = connector.getSourceId();
//            String targetId = connector.getTargetId();
//            this.entryMap.get(sourceId).addOutDegree(connector);
//            this.entryMap.get(targetId).addInDegree(connector);
//        });
    }

    /**
     * 添加连线
     *
     * @param connector 需要添加的连线实例
     * @return 如果连线成功，那么返回值为true，否则返回值为false
     */
    public boolean addConnector(Connector connector) throws FlowDescException {
        if (this.connectors == null) {
            this.connectors = new Connectors();
        }
        try {
            return this.connectors.addConnector(connector);
        } catch (FlowDescException e) {
            throw e;
        }
    }

    /**
     * 创建连线
     *
     * @param srcEntry  源节点
     * @param destEntry 目标节点
     * @return 如果连接成功，返回值为true，否则返回值为false
     * @throws FlowDescException
     */
    public boolean addConnector(IEntry srcEntry, IEntry destEntry) throws FlowDescException {
        return addConnector(new Connector(srcEntry, destEntry));
    }

    /**
     * 创建连线
     *
     * @param srcEntry  源节点
     * @param destEntry 目标节点
     * @return 如果连接成功，返回值为true，否则返回值为false
     * @throws FlowDescException
     */
    public boolean addConnector(IEntry srcEntry, IEntry destEntry, String connectId) throws FlowDescException {
        return addConnector(new Connector(srcEntry, destEntry, connectId));
    }

    /**
     * 创建连写
     *
     * @param srcEntry    源节点
     * @param destEntry   目标节点
     * @param connectType 连线类型
     * @return 如果连线成功，返回值为true，否则返回值为false
     * @throws FlowDescException
     */
    public boolean addConnector(Entry srcEntry, Entry destEntry, Integer connectType) throws FlowDescException {
        Connector connector = new Connector(srcEntry, destEntry);
        connector.setConnectType(connectType);
        return addConnector(connector);
    }

    /**
     * 移除两个节点之间的连线
     *
     * @param srcEntry  源节点
     * @param destEntry 目标节点
     * @return 如果连线移除成功，那么返回值为true，否则返回值为false
     */
    public boolean removeConnector(Entry srcEntry, Entry destEntry) {
        String srcEntryId = srcEntry.getNodeId();
        String destEntryId = destEntry.getNodeId();
        return removeConnector(srcEntryId, destEntryId);
    }

    /**
     * 移除两个节点之间的连线
     *
     * @param srcEntryId  源节点ID
     * @param destEntryId 目标节点ID
     * @return 如果连线移除成功，那么返回值为true，否则返回值为false
     */
    public boolean removeConnector(String srcEntryId, String destEntryId) {
        return this.connectors.removeConnector(srcEntryId, destEntryId);
    }

    /**
     * 获得作业的ID
     *
     * @return 作业ID
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * 设置作业的ID
     *
     * @param jobId 作业的ID
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
        this.nodeId = jobId;
    }

    /**
     * 注意，此处对作业的NodeId进行了置换，即节点的ID就是作业的作业ID
     *
     * @param nodeId 节点的ID
     */
    @Override
    public void setNodeId(String nodeId) {
        this.jobId = nodeId;
        this.nodeId = nodeId;
    }

    /**
     * 绑定多个节点为一个组
     *
     * @param regionId      分片ID
     * @param regionName    分片名称
     * @param masterEntryId 分片的节点ID
     * @param entries       需要绑定到组的节点
     */
    public void group(String regionId, String regionName, String masterEntryId, Entry... entries) {
        ARegion region = new ARegion(regionId, regionName, masterEntryId);
        region.bind(entries);
    }

    /**
     * 解除一整个组
     *
     * @param regionId 组ID
     */
    public void ungroup(String regionId) {
        for (IEntry entry : entryMap.values()) {
            if (((Entry) entry).getRegion() != null && regionId.equals(((Entry) entry).getRegion().getRegionId())) {
                ((Entry) entry).setRegion(null);
            }
        }
    }

    /**
     * 解除一个节点的组特性
     *
     * @param regionId 需要解除的分片ID
     * @param entry    需要解除的节点元素
     */
    public void ungroup(String regionId, Entry entry) {
        ungroup(regionId, entry.getNodeId());
    }

    /**
     * 解除一个节点的组特性
     *
     * @param regionId 需要解除的分片ID
     * @param entryId  需要解除的节点元素ID
     */
    public void ungroup(String regionId, String entryId) {
        IEntry entry = this.getEntry(entryId);
        if (entry != null) {
            ARegion region = ((Entry) entry).getRegion();
            if (region != null && regionId.equals(region.getRegionId())) {
                ((Entry) entry).setRegion(null);
            }
        }
    }

    /**
     * 获得当前作业是否是阻塞的
     *
     * @return 如果是阻塞的，那么返回值为true，否则返回值为为false
     */
    public Boolean getIsObstructive() {
        return isObstructive;
    }

    public void refresh() {
        if (this.connectors != null)
            this.connectors.refresh();
    }

    /**
     * 设置当前作业是否是阻塞的（默认非阻塞型）
     *
     * @param obstructive 如果阻塞，则为true，否则为false
     */
    public void setObstructive(Boolean obstructive) {
        this.isObstructive = obstructive;
    }

    public Boolean getObstructive() {
        return isObstructive;
    }

    public List<IEntry> getSuccessors() {
        if (successors == null) {
            return new ArrayList<>();
        }
        return successors;
    }

    /**
     * 将当前作业的后继节点绑定与父作业，在当前子作业完成后，将拉起这些子作业的直接后继节点
     *
     * @param parentJobId 父作业的作业id
     * @param successors  当前子作业的直接后继
     */
    public void setSuccessors(String parentJobId, List<IEntry> successors) {
        this.successors = successors;
        this.parentJobId = parentJobId;
    }

    /**
     * 停止当前节点的执行器
     *
     * @return 如果停止成功，那么返回值为true，否则返回值为false
     */
    @Override
    public boolean stop() {
        // TODO:
        Integer currentStatus = (Integer) redissonToolkit.getBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_STATUS, nodeId);
        if (EntryStatus.ENTRY_RESUMING.equals(currentStatus)) {
            return false;
        }

        if ((Boolean) this.obtainContext().getAttribute(ATTR_IS_JOB)) {
            List<IEntry> entries = this.getEntries().getEntries();
            for (IEntry entry : entries) {
                entry.setState(EntryStatus.ENTRY_STOPPING);
            }
            return true;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * [WARNING]热更新发布作业[UNSTABLE]
     * <p>
     * 热更新发布作业特性可能需要对整个作业流进行控制重新计算或者是节点裁剪挂载，涉及到的系统变量较多，流程复杂，目前处于实验阶段，生产
     * 运行暂不支持该特性。
     *
     * @return 如果作业更新成功，那么返回值为true，否则返回值为false
     */
    @Override
    public boolean immediateReloading() {
        // 更新一个作业的时候，需要将一个作业下的所有子任务进行暂停操作，并且重新计算各个节点之间的阻塞和控制流因子

        // 作业热更新发布是一个复杂的处理流程

        // 核心的主要思路是
        // 1、暂停作业下的所有子任务;
        // 2、暂停完成后设置即将热更新暂停的状态，等待任务节点状态保存;
        // 3、重新规划作业流控制策略;

        // 4、挂载新增节点;
        // 5、裁剪删除节点;
        // 6、检查各个任务的就绪状态；
        // 7、尝试恢复暂停中的各个任务；
        return super.immediateReloading();
    }

    /**
     * 获得运行时设置的父作业id
     *
     * @return 父作业id
     */
    public String getParentJobId() {
        return parentJobId;
    }

    public Meta meta() {
        return new Meta(Job.class);
    }
}
