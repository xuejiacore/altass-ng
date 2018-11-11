/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.node.domain.connector
 * Author: Xuejia
 * Date Time: 2016/12/15 16:20
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.connector;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.core.domain.Element;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.AbstractPipelineExecutor;
import org.chim.altass.core.executor.AbstractStreamNodeExecutor;

/**
 * Class Name: Connector
 * Create Date: 2016/12/15 16:20
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 常用的连接器对象
 * 连接方式为：两个节点，无自环
 */
@Elem(alias = "connector")
public class Connector extends Element {
    /**
     * 连接线成功
     */
    public static final Integer CONNECT_TYPE_SUCCESS = 0x1;
    /**
     * 连接失败线
     */
    public static final Integer CONNECT_TYPE_FAILURE = 0x2;
    private static final long serialVersionUID = 1481669732486026808L;

    /**
     * 连接线的ID
     */
    @Attr(alias = "connectorId")
    private String connectorId = null;

    /**
     * 上一个节点的ID
     */
    @Attr(alias = "sourceId")
    private String sourceId = null;

    /**
     * 下一个节点的ID
     */
    @Attr(alias = "targetId")
    private String targetId = null;

    /**
     * 连线的类型，成功线0x1，失败线0x2
     */
    @Attr(alias = "connType")
    private Integer connectType = CONNECT_TYPE_SUCCESS;

    /**
     * 创建一个连接器对象
     */
    public Connector() {

    }

    /**
     * 创建一个链接器对象
     *
     * @param sourceEntry 连接器对象的上一个节点
     * @param targetEntry 连接器对象的下一个节点
     * @throws FlowDescException
     */
    public Connector(IEntry sourceEntry, IEntry targetEntry) {
        connect(sourceEntry, targetEntry);
    }

    /**
     * 创建一个链接器对象
     *
     * @param sourceEntry 连接器对象的上一个节点
     * @param targetEntry 连接器对象的下一个节点
     * @param connectorId 连接线的id
     */
    public Connector(IEntry sourceEntry, IEntry targetEntry, String connectorId) {
        connect(sourceEntry, targetEntry, connectorId);
    }

    /**
     * 连接两个元素
     *
     * @param source 连接的来源
     * @param target 连接的目标
     * @return 如果连线创建成功，那么返回值为true，否则返回值为false
     */
    public boolean connect(IEntry source, IEntry target) {
        return this.connect(source, target, generateUUID());
    }

    /**
     * 连接两个元素
     *
     * @param source      连接的来源
     * @param target      连接的目标
     * @param connectorId 连线的id
     * @return 如果连线创建成功，那么返回值为true，否则返回值为false
     */
    public boolean connect(IEntry source, IEntry target, String connectorId) throws FlowDescException {
        if (source == null || target == null) {
            throw new FlowDescException("连接的源和目标不允许为 null");
        } else if (AbstractStreamNodeExecutor.class.isAssignableFrom(source.getExecutorClz())
                && AbstractPipelineExecutor.class.isAssignableFrom(target.getExecutorClz())) {
            throw new FlowDescException("管道流节点直接前驱不支持流式节点. [Pipeline flow nodes do not support flow nodes directly.]");
        } else {
            if (source.equals(target)) {
                throw new FlowDescException("连线:" + getClass().getName() + "不允许出现自环");
            }
            this.sourceId = source.getNodeId();
            this.targetId = target.getNodeId();
            if (this.sourceId != null && this.sourceId.equals(this.targetId)) {
                this.sourceId = this.targetId = null;
                throw new FlowDescException("连线:<" + source.getNodeId() + "->" + target.getNodeId() + ">不允许出现自环");
            }
        }
        // 设置入度和出度
        source.addOutDegree(this);
        target.addInDegree(this);
        this.connectorId = connectorId;
        return true;
    }

    /**
     * 判断当前的连接是否是自环
     *
     * @return 如果当前的连接线构成自环，那么返回值为true，否则返回值为false
     */
    private boolean isSelfLooper() {
        return !(this.sourceId == null || this.targetId == null) && this.sourceId.equals(this.targetId);
    }

    public String getSourceId() {
        return sourceId;
    }

    /**
     * 设置连线的源
     * 该设置方法会检查连线是否形成了自环，如果自环，将会抛出节点描述异常
     *
     * @param sourceId 连线源ID
     */
    public void setSourceId(String sourceId) throws FlowDescException {
        this.sourceId = sourceId;
        if (isSelfLooper()) {
            throw new FlowDescException("连线:" + getClass().getName() + "不允许出现自环");
        }
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) throws FlowDescException {
        this.targetId = targetId;
        if (isSelfLooper()) {
            throw new FlowDescException("连线:" + getClass().getName() + "不允许出现自环");
        }
    }

    public String getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(String connectorId) {
        this.connectorId = connectorId;
    }

    public Integer getConnectType() {
        return connectType;
    }

    /**
     * 设置节点连线的线类型<br/>
     * CONNECT_TYPE_SUCCESS(1)成功;<br/>
     * CONNECT_TYPE_FAILURE(2)失败;<br/>
     * default: CONNECT_TYPE_SUCCESS<br/>
     *
     * @param connectType 连接线的类型
     */
    public void setConnectType(Integer connectType) {
        this.connectType = connectType;
    }
}
