package org.chim.altass.toolkit;


import org.chim.altass.core.manager.UpdatePkgWrapper;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.domain.connector.Connector;
import org.chim.altass.core.executor.general.EndExecutor;
import org.chim.altass.core.executor.general.StartExecutor;
import org.chim.altass.toolkit.job.EntryOperator;
import org.chim.altass.toolkit.job.SearchCallback;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.util.*;

/**
 * Class Name: JobToolkit
 * Create Date: 18-3-13 下午8:17
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:作业操作常用工具箱
 */
public final class JobToolkit {

    /**
     *
     * @param job
     */
    public void rearrangementJob(Job job) {
        // TODO:重新编排作业，调整运行参数
    }

    /**
     * 递归操作前驱
     *
     * @param job     作业
     * @param opEntry 操作节点
     * @param op      操作器
     */
    public static void operateClosestPrecursors(Job job, IEntry opEntry, EntryOperator op) {
        operateClosestPrecursors(job, opEntry, op, opEntry);
    }

    /**
     * 递归操作后继
     *
     * @param job     作业
     * @param opEntry 当前操作节点
     * @param op      操作器
     */
    public static void operateClosestSuccessor(Job job, IEntry opEntry, EntryOperator op) {
        operateClosestSuccessor(job, opEntry, op, opEntry);
    }

    /**
     * 递归操作前驱
     *
     * @param job     作业
     * @param opEntry 操作节点
     * @param op      操作器
     * @param root    递归根节点id
     */
    private static void operateClosestPrecursors(Job job, IEntry opEntry, EntryOperator op, IEntry root) {
        if (opEntry == null) {
            return;
        }
        boolean continuePrecursors = op.operate(opEntry, root);

        List<String> precursorIds = opEntry.obtainPrecursors();
        if (precursorIds == null || precursorIds.size() == 0) {
            return;
        }
        List<IEntry> precursors = job.getEntries(precursorIds);
        for (IEntry precursor : precursors) {
            if (op.onCondition(precursor)) {
                if (continuePrecursors && !precursor.getExecutorClz().isAssignableFrom(StartExecutor.class)) {
                    // 如果操作后返回值为true，那么进行递归当前节点的前驱
                    operateClosestPrecursors(job, precursor, op, root);
                }
            }
        }
    }

    /**
     * 递归操作后继
     *
     * @param job     作业
     * @param opEntry 操作节点
     * @param op      操作器
     * @param root    递归根节点id
     */
    private static void operateClosestSuccessor(Job job, IEntry opEntry, EntryOperator op, IEntry root) {
        if (opEntry == null) {
            return;
        }
        boolean continueSuccessors = op.operate(opEntry, root);

        List<String> successorIds = opEntry.obtainSuccessors();
        if (successorIds == null || successorIds.size() == 0) {
            return;
        }
        List<IEntry> successors = job.getEntries(successorIds);
        for (IEntry successor : successors) {
            if (op.onCondition(successor)) {
                if (continueSuccessors && !successor.getExecutorClz().isAssignableFrom(EndExecutor.class)) {
                    // 如果操作后返回值为true，那么进行递归当前节点的前驱
                    operateClosestSuccessor(job, successor, op, root);
                }
            }
        }
    }

    /**
     * 包装一个作业对象
     *
     * @param job 需要被包装的作业对象
     * @return 包装后的作业对象
     */
    public static JDFWrapper wrap(Job job) {
        return new JDFWrapper(job);
    }

    /**
     * 深复制一个作业
     *
     * @param job 需要被复制的作业
     * @return 复制后的作业
     */
    public static Job copy(Job job) {
        JDFWrapper jdfWrapper = new JDFWrapper(job);
        return jdfWrapper.restore();
    }

    /**
     * 获得作业对比更新包
     *
     * @param old    旧作业元
     * @param update 新作业元
     * @return 对比分析后的作业更新包
     */
    public static UpdatePkgWrapper compareWrap(Job old, Job update) {
        UpdateAnalysis updateAnalysis = compareEntry(old, update);
        return new UpdatePkgWrapper(updateAnalysis);
    }

    /**
     * 用于比对两个作业之间的差异
     * [注意，当前仅比较出新增、删除、变更的连线以及新增、删除的节点]
     *
     * @param oldJob 旧的作业
     * @param newer  变更后的作业
     * @return 打包出来的作业变更信息
     */
    public static UpdateAnalysis compareEntry(Job oldJob, Job newer) {

        if (!oldJob.getJobId().equals(newer.getJobId())) {
            throw new IllegalArgumentException("Must be the same job entry.");
        }

        UpdateAnalysis pkg = new UpdateAnalysis(oldJob.getJobId());

        // 1、递归收集两个作业的基础信息，用于变更比较
        ComparableJobInfo oldJobInfo = collectComparableJobInfo(oldJob);
        ComparableJobInfo newJobInfo = collectComparableJobInfo(newer);

        // 作两个作业节点的交集，用于差集计算增加、删除
        Set<String> retain = new HashSet<>();
        retain.addAll(oldJobInfo.jobEntries);
        retain.retainAll(newJobInfo.jobEntries);
        // 获得删除集
        Set<String> deleteSet = new HashSet<>(oldJobInfo.jobEntries);
        deleteSet.removeAll(retain);
        // 获得新增集
        Set<String> newSet2 = new HashSet<>(newJobInfo.jobEntries);
        newSet2.removeAll(retain);

        // 2、获得连线的比对信息
        ComparableConnectorInfo connectorInfo = collectComparableConnectorInfo(oldJobInfo, newJobInfo);

        pkg.setNewEntries(newer.getEntries(new ArrayList<>(newSet2)));
        pkg.setDeleteEntries(oldJob.getEntries(new ArrayList<>(deleteSet)));
        pkg.setChangedConnectors(new ArrayList<>(connectorInfo.changedConnector.values()));
        pkg.setDelConnectors(new ArrayList<>(connectorInfo.delConnector.values()));
        pkg.setNewConnectors(new ArrayList<>(connectorInfo.newConnector.values()));
        // 分析旧作业树，进行DFS和BFS搜索分析
        treeSearchAnalysis(oldJob, pkg);

        return pkg;
    }


    /**
     * 搜索树分析
     * 对旧作业进行广度优先搜索和深度优先搜索，搜索完成后，搜索优先级配置
     *
     * @param oldJob 分析的是旧作业
     * @param pkg    更新信息包
     */
    @SuppressWarnings("Duplicates")
    private static void treeSearchAnalysis(Job oldJob, UpdateAnalysis pkg) {
        // 广度优先搜索旧作业树，用于挂载新节点的时候处理
        Map<String, Integer> bfsEntryDepthMap = new HashMap<>();
        Map<String, Integer> dfsEntryDepthMap = new HashMap<>();

        // 广度优先搜索旧作业树，用于新增添加节点，取最小深度
        pkg.setBfsDepth(BFS(oldJob, new TreeSearchAnalysis(bfsEntryDepthMap)));
        List<Integer> bfsEntryIdsSorted = new ArrayList<>(bfsEntryDepthMap.values());
        bfsEntryIdsSorted.sort(Comparator.comparingInt(o -> o));

        // 深度优先搜索旧作业树，用于删除释放节点，取最大深度
        pkg.setDfsDepth(DFS(oldJob, new TreeSearchAnalysis(dfsEntryDepthMap)));
        List<Integer> dfsEntryIdsSorted = new ArrayList<>(dfsEntryDepthMap.values());
        dfsEntryIdsSorted.sort((o1, o2) -> o2 - o1);

        // 对广度优先搜索的结果进行升序排序
        List<String> bfsEntryIdSorted = new ArrayList<>();
        for (Integer priority : bfsEntryIdsSorted) {
            for (String key : bfsEntryDepthMap.keySet()) {
                if (priority.equals(bfsEntryDepthMap.get(key))) {
                    if (bfsEntryIdSorted.contains(key)) {
                        continue;
                    }
                    bfsEntryIdSorted.add(key);
                }
            }
        }

        // 对深度优先搜索结果进行降序排序
        List<String> dfsEntryIdSorted = new ArrayList<>();
        for (Integer priority : dfsEntryIdsSorted) {
            for (String key : dfsEntryDepthMap.keySet()) {
                if (priority.equals(dfsEntryDepthMap.get(key))) {
                    if (dfsEntryIdSorted.contains(key)) {
                        continue;
                    }
                    dfsEntryIdSorted.add(key);
                }
            }
        }

        // 设置DFS和BFS搜索结果的优先级排序
        pkg.setBfsSorted(bfsEntryIdSorted);
        pkg.setDfsSorted(dfsEntryIdSorted);
    }

    /**
     * 广度优先搜索树
     *
     * @param tree     搜索tree
     * @param callback 搜索回调，可用于各种处理
     */
    @SuppressWarnings("ConfusingArgumentToVarargsMethod")
    public static int BFS(Job tree, SearchCallback callback) {
        int depth = 1;
        return BFS(tree, callback, depth, null);
    }

    /**
     * 深度优先搜索树
     *
     * @param tree     搜索的作业树
     * @param callback 搜索回调
     */
    public static int DFS(Job tree, SearchCallback callback) {
        int depth = 1;
        return DFS(tree, null, depth, callback);
    }

    // -----------------------------------------------------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * 广度优先搜索树
     *
     * @param callback 搜索回调
     * @param depth    深度
     * @param target   搜索的作业
     */
    private static int BFS(Job tree, SearchCallback callback, int depth, IEntry... target) {
        IEntry[] processEntry = target;
        List<String> successorIds;
        List<IEntry> entries;

        if (target == null) {
            Entry startNode = tree.getStartNode();
            processEntry = new IEntry[]{startNode};
            successorIds = startNode.obtainSuccessors();
            callback.node(startNode, successorIds == null || successorIds.size() == 0, depth);
        }

        List<Connector> nextConnectors = new ArrayList<>();
        List<IEntry> nextEntries = new ArrayList<>();
        Set<String> connectIds = new HashSet<>();
        Set<String> nextEntryIds = new HashSet<>();

        // 对每一个发射点进行横向聚合
        for (IEntry iEntry : processEntry) {
            successorIds = iEntry.obtainSuccessors();
            if (successorIds == null || successorIds.size() == 0) {
                continue;
            }

            entries = tree.getEntries(successorIds);
            if (entries == null || entries.size() == 0) {
                continue;
            }

            for (IEntry entry : entries) {
                if (nextEntryIds.add(entry.getNodeId())) {
                    List<Connector> outDegree = entry.getOutDegree();
                    for (Connector connector : outDegree) {
                        if (connectIds.add(connector.getConnectorId())) {
                            // 收集连线
                            nextConnectors.add(connector);
                        }
                    }
                    // 收集节点
                    nextEntries.add(entry);
                }
            }
        }

        depth++;
        if (nextConnectors.size() == 0 || nextEntries.size() == 0) {
            return depth;
        }


        for (IEntry nextEntry : nextEntries) {
            successorIds = nextEntry.obtainSuccessors();
            // 回调节点
            callback.node(nextEntry, successorIds == null || successorIds.size() == 0, depth);
        }
        for (Connector nextConnector : nextConnectors) {
            // 回调连线
            callback.connector(nextConnector);
        }

        // 继续递归获得下层节点及连线信息
        IEntry[] recursiveEntries = new IEntry[nextEntries.size()];
        int index = 0;
        for (IEntry nextEntry : nextEntries) {
            recursiveEntries[index++] = nextEntry;
        }
        return BFS(tree, callback, depth, recursiveEntries);
    }

    /**
     * 深度优先搜索树
     *
     * @param tree     搜索的作业树
     * @param target   搜索目标
     * @param callback 搜索回调
     */
    @SuppressWarnings("SameParameterValue")
    private static int DFS(Job tree, IEntry target, int depth, SearchCallback callback) {
        IEntry processEntry = target;
        List<String> successorIds;
        if (target == null) {
            processEntry = tree.getStartNode();
            successorIds = processEntry.obtainSuccessors();
            callback.node(processEntry, successorIds == null || successorIds.size() == 0, depth);
        }

        List<Connector> outDegree = processEntry.getOutDegree();
        List<Integer> maxDepth = new ArrayList<>();
        if (outDegree != null && outDegree.size() > 0) {

            for (Connector connector : outDegree) {
                callback.connector(connector);

                String targetId = connector.getTargetId();
                IEntry targetEntry = tree.getEntry(targetId);
                if (targetEntry != null) {
                    successorIds = targetEntry.obtainSuccessors();
                    callback.node(targetEntry, successorIds == null || successorIds.size() == 0, depth + 1);
                    maxDepth.add(DFS(tree, targetEntry, depth + 1, callback));
                }
            }
        }
        Integer max = depth;
        for (Integer integer : maxDepth) {
            max = Math.max(max, integer);
        }
        return max;
    }

    /**
     * 递归收集对比作业时使用的作业信息
     *
     * @param job 需要收集信息的作业
     * @return 收集后的作业信息
     */
    private static ComparableJobInfo collectComparableJobInfo(Job job) {
        return collectComparableJobInfo(job, job.getStartNode(), null);
    }

    /**
     * 递归收集比较使用的作业信息
     *
     * @param job     需要收集信息的作业
     * @param entry   当前节点
     * @param jobInfo 作业信息
     * @return 作业信息
     */
    private static ComparableJobInfo collectComparableJobInfo(Job job, IEntry entry, ComparableJobInfo jobInfo) {
        if (jobInfo == null) {
            jobInfo = new ComparableJobInfo(job);
        }
        List<Connector> outDegree = entry.getOutDegree();
        jobInfo.jobEntries.add(entry.getNodeId());
        for (Connector connector : outDegree) {
            String connectorId = connector.getConnectorId();
            jobInfo.connectorMapper.put(connectorId, connector);
            jobInfo.jobEntries.add(connector.getTargetId());
        }
        List<String> successorIds = entry.obtainSuccessors();
        List<IEntry> successors = jobInfo.currentJob.getEntries(successorIds);
        if (successors != null) {
            for (IEntry successor : successors) {
                collectComparableJobInfo(jobInfo.currentJob, successor, jobInfo);
            }
        }
        return jobInfo;
    }

    /**
     * 从作业对比信息中，收集连接线的差异信息
     *
     * @param oldJobInfo 源作业比对信息
     * @param newJobInfo 新作业比对信息
     * @return 连线的差异信息
     */
    private static ComparableConnectorInfo collectComparableConnectorInfo(ComparableJobInfo oldJobInfo,
                                                                          ComparableJobInfo newJobInfo) {
        ComparableConnectorInfo info = new ComparableConnectorInfo();

        Map<String, Connector> oldConnectors = oldJobInfo.connectorMapper;
        Map<String, Connector> newConnectors = newJobInfo.connectorMapper;

        // 筛选出新增的、删除的以及类型变更的连接线
        Set<String> oldConnectorIds = oldConnectors.keySet();
        Set<String> newConnectorIds = newConnectors.keySet();

        Set<String> retain = new HashSet<>(oldConnectorIds);
        retain.retainAll(newConnectorIds);

        // 被删除的连线
        Set<String> delConnectorIds = new HashSet<>(oldConnectorIds);
        delConnectorIds.removeAll(retain);
        for (String delConnectorId : delConnectorIds) {
            info.delConnector.put(delConnectorId, oldConnectors.get(delConnectorId));
        }

        // 新增的连线
        Set<String> genConnectorIds = new HashSet<>(newConnectorIds);
        genConnectorIds.removeAll(retain);
        for (String genConnectorId : genConnectorIds) {
            info.newConnector.put(genConnectorId, newConnectors.get(genConnectorId));
        }

        for (String connectorId : retain) {
            Connector oldConnector = oldConnectors.get(connectorId);
            Connector newConnector = newConnectors.get(connectorId);
            if (oldConnector != null && newConnector != null) {
                if (!oldConnector.getConnectType().equals(newConnector.getConnectType())) {
                    info.changedConnector.put(connectorId, newConnector);
                }
            }
        }
        return info;
    }

    /**
     * 作业的差异信息结构
     */
    static class ComparableJobInfo {
        Job currentJob = null;                                          // 当前比对的作业
        Map<String, Connector> connectorMapper = null;                  // 当前作业的连线映射
        Set<String> jobEntries = null;                                  // 当前作业的执行集合

        ComparableJobInfo(Job job) {
            this.currentJob = job;
            this.connectorMapper = new HashMap<>();
            this.jobEntries = new HashSet<>();
        }
    }

    /**
     * 连接线的差异信息结构
     */
    static class ComparableConnectorInfo {
        Map<String, Connector> delConnector = null;                     // 被删除的连线
        Map<String, Connector> newConnector = null;                     // 新增的连线
        Map<String, Connector> changedConnector = null;                 // 变更的连线

        ComparableConnectorInfo() {
            this.delConnector = new HashMap<>();
            this.newConnector = new HashMap<>();
            this.changedConnector = new HashMap<>();
        }
    }

    static class TreeSearchAnalysis implements SearchCallback {

        // 存储深度映射关系
        private Map<String, Integer> entryDepthMap = null;

        TreeSearchAnalysis(Map<String, Integer> map) {
            this.entryDepthMap = map;
        }

        @Override
        public void node(IEntry entry, boolean isLeaf, Integer hierarchy) {
            // 配置节点的优先级，取节点最小层次，取最小深度
            String nodeId = entry.getNodeId();
            Integer layer = entryDepthMap.get(nodeId);
            entryDepthMap.put(nodeId, layer != null ? Math.min(hierarchy, layer) : hierarchy);
        }

        @Override
        public void connector(Connector connector) {
        }
    }
}
