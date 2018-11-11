package org.chim.altass.core.algorithm;

/**
 * 图的顶点
 */
public class GraphNode {
    private int pre;
    private int post;

    //顶点名  
    private int name;

    //顶点信息  
    public String getInfo() {
        return "vartex: " + name + "\t pre= " + pre + ", post= " + post;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getPre() {
        return pre;
    }

    public void setPre(int pre) {
        this.pre = pre;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }
}  