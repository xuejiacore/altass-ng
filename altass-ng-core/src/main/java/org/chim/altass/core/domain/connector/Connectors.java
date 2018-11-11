/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.domain.connector
 * Author: Xuejia
 * Date Time: 2016/12/27 10:03
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.connector;


import org.chim.altass.core.algorithm.GraphDAG;
import org.chim.altass.core.algorithm.GraphDFS;
import org.chim.altass.core.algorithm.GraphNode;
import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.core.domain.Element;
import org.chim.altass.core.exception.CyclicityException;
import org.chim.altass.core.exception.FlowDescException;

import java.util.*;

/**
 * Class Name: Connectors
 * Create Date: 2016/12/27 10:03
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Elem(alias = "connectors")
public class Connectors extends Element {

    private static final long serialVersionUID = 1104070042758820542L;

    /**
     * 连接对象
     */
    @Elem(alias = "connector")
    private List<Connector> connectors = null;

    /**
     * 连接对象的映射，辅助查找添加或删除连线
     */
    private Map<String, Connector> connectorMap = null;

    // 有效顶点数量
    private int nodeIdx = 0;
    // 节点与顶点索引号映射关系
    private Map<String, Integer> matrixIdxMapper = null;

    public Connectors() {
        matrixIdxMapper = new HashMap<String, Integer>();
        connectorMap = new HashMap<String, Connector>();
    }

    public void refresh() {
        for (Connector connector : connectors) {
            // 添加节点的映射关系
            if (matrixIdxMapper.get(connector.getSourceId()) == null) {
                matrixIdxMapper.put(connector.getSourceId(), nodeIdx++);
            }
            if (matrixIdxMapper.get(connector.getTargetId()) == null) {
                matrixIdxMapper.put(connector.getTargetId(), nodeIdx++);
            }
            this.connectorMap.put(connector.getConnectorId(), connector);
        }
    }

    /**
     * 添加一个连接器对象
     *
     * @param connector 连接器对象
     * @return 如果添加成功，那么返回值为true，否则返回值为false
     */
    public boolean addConnector(Connector connector) throws FlowDescException {
        if (this.connectors == null) {
            this.connectors = new ArrayList<>();
        }
        if (this.connectorMap == null) {
            this.connectorMap = new HashMap<>();
        }

        this.connectorMap.put(connector.getConnectorId(), connector);
        boolean addResult = this.connectors.add(connector);
        // 如果连线添加失败，那么需要移除映射关系中的数据
        if (!addResult) {
            this.connectorMap.remove(connector.getConnectorId());
            return false;
        }

        // 添加节点的映射关系
        if (matrixIdxMapper.get(connector.getSourceId()) == null) {
            matrixIdxMapper.put(connector.getSourceId(), nodeIdx++);
        }
        if (matrixIdxMapper.get(connector.getTargetId()) == null) {
            matrixIdxMapper.put(connector.getTargetId(), nodeIdx++);
        }

        try {
            // 有向无环图判定，正常作业不允许存在环的存在，防止作业执行循环或者阻塞节点无法计算阻塞因子
            this.directedAcyclicDetector();
        } catch (FlowDescException e) {
            // 有向循环图存在，不允许创建该连线，并抛出流程描述异常
            this.connectorMap.remove(connector.getConnectorId());
            this.connectors.remove(connector);

            throw e;
        }

        return true;
    }

    /**
     * 根据连接器的连接ID从当前的连接器集合中移除一个连接器
     *
     * @param connectorId 连接器的连接ID
     * @return 如果移除成功，那么返回值为true，否则返回值为false
     */
    public boolean removeConnector(String connectorId) {
        if (this.connectors == null || this.connectors.size() == 0) {
            return false;
        } else {
            Connector connector = this.connectorMap.get(connectorId);
            return connector != null && this.connectors.remove(connector);
        }
    }

    public boolean removeConnector(String sourceId, String targetId) {
        Connector connectorWillBeRm = null;
        for (Connector connector : this.connectors) {
            if (sourceId.equals(connector.getSourceId()) &&
                    targetId.equals(connector.getTargetId())) {
                connectorWillBeRm = connector;
                break;
            }
        }
        return connectorWillBeRm != null
                && connectorMap.remove(connectorWillBeRm.getConnectorId()) != null
                && connectors.remove(connectorWillBeRm);
    }

    /**
     * 有向无环图判定，正常作业不允许存在环的存在，防止作业执行循环或者阻塞节点无法计算阻塞因子
     *
     * @throws FlowDescException 流程描述异常
     */
    private void directedAcyclicDetector() throws FlowDescException {
        //第一行的数字是顶点的数目
        int v = matrixIdxMapper.size();
        //第二行的数字是边的数目
        int e = this.connectors.size();
        GraphDAG graph = new GraphDAG(v, e);

        //读取每条边对应的两个顶点,设置邻接矩阵的值
        for (Connector connector : connectors) {
            graph.addEdge(matrixIdxMapper.get(connector.getSourceId()), matrixIdxMapper.get(connector.getTargetId()));
        }

        //根据graph的邻接矩阵,对其进行深度优先搜索
        int matrix[][] = graph.getAdjacencyMatrix();
        GraphDFS graphDFS = new GraphDFS(matrix);

        //使用无向图的深度优先搜索,对该有向图进行(参照"第四周作业——无向图的DFS算法")
        graphDFS.dfs();

        //强连通分量集合
        List<GraphNode[]> cc = graphDFS.getCC();
        List<String> pathList = graph.getCyclePath(cc);

        if (pathList.size() > 0) {
            for (String path : pathList) {
                String[] split = path.split("-");

                StringBuilder idPath = new StringBuilder();
                for (String id : split) {
                    for (Map.Entry<String, Integer> entry : matrixIdxMapper.entrySet()) {
                        if (String.valueOf(entry.getValue()).equals(id)) {
                            idPath.append("->").append(entry.getKey());
                        }
                    }
                }
                if (idPath.length() > 0) {
                    // 检查到环的存在
                    String troubleNodeData = idPath.substring(2);
                    CyclicityException exception = new CyclicityException("[DAG] The closed loop is not allowed in the modification of graph: " + troubleNodeData);
                    exception.setTroubleNode(Arrays.asList(troubleNodeData.split("->")));
                    throw exception;
                }
            }
        }
        // 没有检查到环的存在
    }

    public boolean removeConnector(Connector connector) {
        return !(this.connectors == null || this.connectors.size() == 0) && this.connectors.remove(connector);
    }

    public List<Connector> getConnectors() {
        return connectors = connectors == null ? new ArrayList<>() : connectors;
    }

    public void setConnectors(List<Connector> connectors) {
        this.connectors = connectors;
    }
}
