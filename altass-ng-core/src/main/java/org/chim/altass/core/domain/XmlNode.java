/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.node.domain
 * Author: Xuejia
 * Date Time: 2016/12/15 10:53
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain;

import com.google.gson.Gson;
import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.utils.type.NumberUtil;

import java.io.Serializable;
import java.util.UUID;

/**
 * Class Name: XmlNode
 * Create Date: 2016/12/15 10:53
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * xml抽象节点，该节点是所有绘制节点的原始基类
 * <p>
 * 节点元数据：
 * <node nodeId="" desc="" nodeName="" nodeType=""></node>
 */
public abstract class XmlNode implements Serializable {

    private static final String NUMBER_FORMATTER = "0000000000";

    /**
     * TODO:此处是否存在风险
     * 考虑在大量节点进行json化的时候的共享变量同步问题
     */
    private static final Gson gson = new Gson();
    private static final long serialVersionUID = -7665553801772635677L;

    /**
     * 节点ID
     */
    @Attr(alias = "nodeId")
    protected String nodeId = null;

    /**
     * 节点的描述信息
     */
    @Attr(alias = "desc")
    private String desc = null;

    /**
     * 节点的名称
     */
    @Attr(alias = "nodeName")
    private String nodeName = null;

    /**
     * 节点的类型
     */
    @Attr(alias = "nodeType")
    protected Integer nodeType = null;

    /**
     * 生成ID
     *
     * @return 返回不带'-'的UUID
     */
    protected String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    protected String numberFmt(int num) {
        return NumberUtil.format(NUMBER_FORMATTER, num);
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    public String toJson() {
        return gson.toJson(this);
    }
}
