/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.domain
 * Author: Xuejia
 * Date Time: 2016/12/19 11:40
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain;


import org.chim.altass.core.domain.connector.Connector;
import org.chim.altass.core.manager.NodeResourceManager;
import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.core.configuration.NodeResource;
import org.chim.altass.core.executor.AbstractExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class Name: Node
 * Create Date: 2016/12/19 11:40
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 所有节点的基类
 */
@SuppressWarnings("unchecked")
public abstract class Node extends Element2D implements IEntry {
    private static final long serialVersionUID = -6131836184048844893L;
    private NodeResource nodeResource = NodeResourceManager.getInstance().getResource((Class<?>) super.getClass());

    /**
     * 节点的入度列表
     */
    @Elem(alias = "inDegree")
    private List<Connector> inDegree = null;

    /**
     * 节点的出度列表
     */
    @Elem(alias = "outDegree")
    private List<Connector> outDegree = null;

    public Node() {
    }

    public Node(Integer x, Integer y) {
        super(x, y);
    }

    /**
     * 获得当前节点的前驱节点ID集合
     *
     * @return 当前节点的前驱节点ID集合
     */
    public List<String> obtainPrecursors() {
        List<String> entryIds = null;
        List<Connector> inDegree = this.getInDegree();                  // 获得入度

        if (inDegree != null && inDegree.size() != 0) {
            entryIds = new ArrayList<>();
            entryIds.addAll(inDegree.stream().map(Connector::getSourceId).collect(Collectors.toList()));
        }
        return entryIds;
    }

    /**
     * 获得指定节点下，向后扇形搜索制定类名对应的节点列表，直到搜索到为止
     *
     * @param root        搜索的根节点
     * @param execClzName 搜索的节点对应的执行器的执行类名
     * @param job         节点对应的作业
     * @return 辐射搜索得到的节点列表
     */
    public List<IEntry> getNextEntry(Job job, IEntry root, String execClzName, boolean containSelf) {
        return getNextEntry(root, execClzName, job, new ArrayList<>(), containSelf);
    }

    /**
     * 该方法为递归方法
     * <p>
     * 获得指定节点下，向下搜索制定执行器类名对应的节点列表，直到搜索到为止
     *
     * @param root          搜索的根节点
     * @param execClzName   搜索的节点对应执行器的执行类名
     * @param job           节点对应的作业
     * @param nextFoundList 搜索到的节点列表
     * @return 辐射搜索得到的节点列表
     */
    private List<IEntry> getNextEntry(IEntry root, String execClzName, Job job, List<IEntry> nextFoundList, boolean containSelf) {
        List<IEntry> nextFoundedEntries = nextFoundList;
        if (nextFoundedEntries == null) {
            nextFoundedEntries = new ArrayList<>();
        }

        Class<?> executorClz = null;
        try {
            executorClz = Class.forName(execClzName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (containSelf && executorClz != null && executorClz.isAssignableFrom(root.getExecutorClz())) {
            nextFoundList.add(root);
            return nextFoundList;
        }
        List<Connector> outDegree = root.getOutDegree();
        for (Connector connector : outDegree) {
            IEntry entry = job.getEntry(connector.getTargetId());
            if (executorClz != null && executorClz.isAssignableFrom(entry.getExecutorClz())) {
                nextFoundedEntries.add(entry);
            } else {
                getNextEntry(entry, execClzName, job, nextFoundedEntries, false);
            }
        }
        return nextFoundedEntries;
    }

    /**
     * 获取大概年前节点的前驱成功节点的ID集合
     *
     * @param connectorType 连接的类型，1成功；2失败
     * @return 返回满足连接类型的连接线集合
     */
    public List<String> obtainProcessorsByType(Integer connectorType) {
        List<String> entryIds = null;
        List<Connector> inDegree = this.getInDegree();

        if (inDegree != null && inDegree.size() != 0) {
            entryIds = new ArrayList<>();
            entryIds.addAll(inDegree.stream()
                    .filter(connector -> Objects.equals(connector.getConnectType(), connectorType))
                    .map(Connector::getSourceId).collect(Collectors.toList()));
        }
        return entryIds;
    }

    /**
     * 获得当前节点的后继节点ID集合
     *
     * @return 当前节点的后继节点ID集合
     */
    public List<String> obtainSuccessors() {
        List<String> entryIds = null;
        List<Connector> outDegree = this.getOutDegree();

        if (outDegree != null && outDegree.size() != 0) {
            entryIds = new ArrayList<>();
            entryIds.addAll(outDegree.stream().map(Connector::getTargetId).collect(Collectors.toList()));
        }
        return entryIds;
    }

    public boolean hasSuccessorsByType(Integer connectorType) {
        if (outDegree != null) {
            for (Connector connector : outDegree) {
                if (Objects.equals(connector.getConnectType(), connectorType)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获得当前节点的后继节点的ID集合
     *
     * @param connectorType 当前连接线的类型
     * @return 当前节点的后继节点ID集合
     */
    public List<String> obtainSuccessorsByType(Integer connectorType, boolean revert) {
        List<String> entryIds = null;
        List<Connector> outDegree = this.getOutDegree();
        if (outDegree != null && outDegree.size() != 0) {
            entryIds = new ArrayList<>();
            entryIds.addAll(outDegree.stream().filter(connector -> revert != Objects.equals(connector.getConnectType(), connectorType))
                    .map(Connector::getTargetId).collect(Collectors.toList()));
        }
        return entryIds;
    }

    public void setNodeResource(NodeResource resource) {
        this.nodeResource = resource;
    }

    public NodeResource getNodeResource() {
        if (this.nodeResource == null) {
            // 如果获取不到资源配置，从子类中指定执行器中获得
            nodeResource = NodeResourceManager.getInstance().getResource(getExecutorClz());
        }
        return this.nodeResource;
    }

    /**
     * 从子类中获得执行器
     *
     * @return 获得真正的执行器
     */
    public abstract Class<? extends AbstractExecutor> getExecutorClz();

    public void clearInOutDegree() {
        this.inDegree.clear();
        this.outDegree.clear();
    }

    /**
     * 添加一个入度
     *
     * @param connector 入度连线
     * @return 如果添加成功，那么返回值为true，否则返回值为false
     */
    public boolean addInDegree(Connector connector) {
        if (this.inDegree == null) {
            this.inDegree = new ArrayList<>();
        }
        return !this.inDegree.contains(connector) && this.inDegree.add(connector);
    }

    @Override
    public boolean delOutDegree(Connector connector) {
        if (this.outDegree == null) {
            return false;
        }
        List<Connector> rmList = new ArrayList<>();
        for (Connector conn : outDegree) {
            if (conn.getConnectorId().equals(connector.getConnectorId())) {
                rmList.add(conn);
            }
        }
        outDegree.removeAll(rmList);
        return true;
    }

    @Override
    public boolean delInDegree(Connector connector) {
        if (this.inDegree == null) {
            return false;
        }
        List<Connector> rmList = new ArrayList<>();
        for (Connector conn : inDegree) {
            if (conn.getConnectorId().equals(connector.getConnectorId())) {
                rmList.add(conn);
            }
        }
        inDegree.removeAll(rmList);
        return true;
    }

    /**
     * 添加一个出度连线
     *
     * @param connector 出度连线
     * @return 如果添加成功，那么返回值为true，否则返回值为false
     */
    public boolean addOutDegree(Connector connector) {
        if (this.outDegree == null) {
            this.outDegree = new ArrayList<>();
        }
        return !this.outDegree.contains(connector) && this.outDegree.add(connector);
    }

    public List<Connector> getInDegree() {
        return inDegree = inDegree == null ? new ArrayList<>() : inDegree;
    }

    public void setInDegree(List<Connector> inDegree) {
        this.inDegree = inDegree;
    }

    public List<Connector> getOutDegree() {
        return outDegree = outDegree == null ? new ArrayList<>() : outDegree;
    }

    public void setOutDegree(List<Connector> outDegree) {
        this.outDegree = outDegree;
    }
}
