/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.node.domain
 * Author: Xuejia
 * Date Time: 2016/12/15 11:06
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;

/**
 * Class Name: Element
 * Create Date: 2016/12/15 11:06
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * 元素节点是所有XML节点的基类，后续继承节点需要添加 @Element 注解才能够进行节点信息转化
 * <p>
 * 元数据：
 * <foo state="val" nodeId="" desc="" nodeName="" nodeType="">
 * <description>val</description>
 * </foo>
 */
public abstract class Element extends XmlNode {
    private static final long serialVersionUID = 9156756698764728567L;
    /**
     * 节点状态
     */
    @Attr(alias = "state")
    private Integer state = null;

    /**
     * 节点的描述子节点
     */
    @Elem(alias = "description")
    private Description description = null;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    /**
     * 获得节点元素的描述文本
     *
     * @return 节点元素的描述文本
     */
    public String getDescriptionText() {
        return this.description == null ? null : description.getText();
    }

    /**
     * 设置描述节点（文本）
     *
     * @param text 需要设置的描述信息
     */
    public void setDescriptionText(String text) {
        if (this.description == null) {
            this.description = new Description(text);
        } else {
            this.description.setText(text);
        }
    }

}
