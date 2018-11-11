package org.chim.altass.core.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 有向无环图的判断
 */
public class GraphDAG {

    //vertex,顶点数目  
    private int v;
    //edge,边数目  
    private int e;
    //邻接矩阵  
    private int[][] matrix;

    /**
     * 初始化
     *
     * @param vertex 顶点数目
     * @param edge   边数目
     */
    public GraphDAG(int vertex, int edge) {
        if (edge < 0) throw new RuntimeException("Number of edges must be nonnegative");
        if (edge > vertex * vertex) throw new RuntimeException("Too many edges");

        this.v = vertex;
        this.e = edge;
        this.matrix = new int[v][v];
    }

    //两个顶点间存在边,设置矩阵值  
    public void addEdge(int v1, int v2) {
        matrix[v1][v2] = 1;
    }

    //获取邻接矩阵  
    public int[][] getAdjacencyMatrix() {
        return matrix;
    }

    /**
     * 获取环路路径
     *
     * @param cc 强连通分量集合
     * @return 各个强连通分量的环路路径
     */
    public List<String> getCyclePath(List<GraphNode[]> cc) {
        List<String> list = new ArrayList<String>();
        int count = cc.size();

        //遍历每个强连通分量,在其内部寻找环路  
        for (int i = 0; i < count; i++) {
            GraphNode[] c = cc.get(i);
            int len = c.length;

            //取出强连通分量集合的所有顶点名-->数组,方便查找  
            int[] sortSequence = new int[len];
            int num = 0;
            //  
            //sortSequence:  
            //name :0   1   5   4   2   3  
            //index:0   1   2   3   4   5  
            //  
            for (GraphNode node : c) {
                sortSequence[num++] = node.getName();
            }

            for (int j = 0; j < len; j++) {
                //取出边(u,v)的u顶点  
                int vertex1 = sortSequence[j];
                for (int k = 0; k < len; k++) {
                    //取出边(u,v)的v顶点  
                    int vertex2 = sortSequence[k];

                    //关键!!! 在有向无环图中,每条边都指向一个post值更小的顶点( post(u)>post(v) )  
                    //即对于回边有: post(u)<post(v)~~~  
                    if (matrix[vertex1][vertex2] == 1 && c[j].getPost() < c[k].getPost()) {

                        StringBuffer sb = new StringBuffer();
                        for (int n = k; n < j; n++) {
                            //注意!!! 环的路径中,每条边都指向一个post值更小的顶点  
                            if (c[n].getPost() > c[n + 1].getPost()) {
                                sb.append(sortSequence[n] + "-");
                            }
                        }
                        sb.append(vertex1 + "-" + vertex2);
                        list.add(sb.toString());
                    }
                }
            }

        }

        return list;
    }

}  