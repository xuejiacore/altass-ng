/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.node.domain
 * Author: Xuejia
 * Date Time: 2016/12/15 16:46
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.core.domain.event.NodeEvent;
import org.chim.altass.core.domain.event.NodeEvents;
import org.chim.altass.core.domain.meta.InputParam;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.domain.meta.OutputParam;

/**
 * Class Name: Element2D
 * Create Date: 2016/12/15 16:46
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 二维可绘图节点
 * <p>
 * 所有的页面上的可绘制的节点都要继承该 Element2D
 */
public abstract class Element2D extends Element {

    private static final long serialVersionUID = 7286809357226083974L;
    /**
     * 节点的横坐标
     */
    @Attr(alias = "x")
    private Integer x = null;

    /**
     * 节点的纵坐标
     */
    @Attr(alias = "y")
    private Integer y = null;

    /**
     * 输入参数
     */
    @Elem(alias = "inputParams")
    private InputParam inputParam = null;

    /**
     * 输出参数
     */
    @Elem(alias = "outputParams")
    private OutputParam outputParam = null;

    /**
     * 需要处理的事件
     */
    @Elem(alias = "events")
    private NodeEvents events = null;

    /**
     * 无参构造
     */
    public Element2D() {
    }

    /**
     * 指定节点的横纵坐标
     *
     * @param x 横坐标
     * @param y 纵坐标
     */
    public Element2D(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public InputParam getInputParam() {
        return inputParam;
    }

    public void setInputParam(InputParam inputParam) {
        this.inputParam = inputParam;
    }

    public OutputParam getOutputParam() {
        return outputParam;
    }

    public void setOutputParam(OutputParam outputParam) {
        this.outputParam = outputParam;
    }

    public NodeEvents getEvents() {
        return events;
    }

    public void setEvents(NodeEvents events) {
        this.events = events;
    }

    /**
     * 添加处理事件
     *
     * @param event 需要添加的事件
     */
    public void addEvent(NodeEvent event) {
        if (this.events == null) {
            this.events = new NodeEvents();
        }
        this.events.addEvent(event);
    }

    /**
     * 添加输入参数
     *
     * @param param 需要添加的输入参数
     */
    public void addInputParameter(MetaData param) {
        if (this.inputParam == null) {
            this.inputParam = new InputParam();
        }
        this.inputParam.addParameter(param);
    }

    /**
     * 添加输出参数
     * 在元素中直接添加输出参数，有可能会被节点执行器在执行过程中覆盖
     *
     * @param param 输出参数
     */
    public void addOutputParameter(MetaData param) {
        if (this.outputParam == null) {
            this.outputParam = new OutputParam();
        }
        this.outputParam.addParameter(param);
    }
}
