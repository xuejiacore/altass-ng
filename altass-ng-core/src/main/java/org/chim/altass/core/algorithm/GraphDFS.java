package org.chim.altass.core.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 无向图的DFS算法
 */
public class GraphDFS {
    private boolean[] visited;

    //邻接矩阵  
    private int[][] matrix;

    //pre和post指针  
    private int _pre_post = 1;

    //连通分量指针,指向每个连通分量在顶点集合nodes里的开始位置  
    private int _cc = 0;

    //所有顶点的集合  
    private GraphNode[] nodes;

    //连通分量集合  
    private List<GraphNode[]> cc = new ArrayList<GraphNode[]>();

    /**
     * 初始化
     *
     * @param matrix 图的邻接矩阵
     */
    public GraphDFS(int[][] matrix) {
        visited = new boolean[matrix.length];

        List<GraphNode> list = new ArrayList<GraphNode>();
        for (int i = 0; i < matrix.length; i++) {
            list.add(new GraphNode());
        }
        nodes = list.toArray(new GraphNode[0]);
        //nodes = new GraphNode[v];  

        this.matrix = matrix;
    }

    /**
     * 对某个连通分量进行深度优先搜索
     *
     * @param v 第几个顶点
     */
    public void DFSTraverse(int v) {
        visited[v] = true;
        nodes[_cc].setName(v);
        int tmp = _cc;

        //记录当前遍历到的顶点的pre  
        nodes[tmp].setPre(_pre_post++);
        _cc++;

        //对当前顶点的邻接顶点再进行深度优先搜索  
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[v][i] == 1 && !visited[i])
                DFSTraverse(i);
        }

        //记录当前遍历到的顶点的post  
        nodes[tmp].setPost(_pre_post++);
    }

    /**
     * 对所有连通分量进行深度优先搜索
     */
    public void dfs() {
        for (int i = 0; i < nodes.length; i++) {
            if (!visited[i]) {
                //该连通分量在nodes集合的开始位置  
                int start = _cc;
                //对该连通分量进行深度遍历  
                DFSTraverse(i);
                //该连通分量的大小  
                int count = _cc - start;

                //该连通分量包含的所有顶点  
                GraphNode[] newNodes = new GraphNode[count];
                for (int j = 0; j < count; j++) {
                    int v = start + j;
                    newNodes[j] = nodes[v];
                }

                cc.add(newNodes);
            }
        }
    }

    //cc: Connected Component,连通分量  
    public List<GraphNode[]> getCC() {
        return cc;
    }

}  