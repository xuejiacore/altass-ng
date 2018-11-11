/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml.domain
 * Author: Xuejia
 * Date Time: 2016/12/10 18:35
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml.domain;


import org.chim.altass.base.parser.xml.annotation.AttributeType;

/**
 * Class Name: XmlAttribute
 * Create Date: 2016/12/10 18:35
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * XML的属性字段
 */
public class XmlAttribute extends XmlNode {

    /**
     * 属性的数据类型
     */
    private AttributeType attributeType = AttributeType.TEXT;

    public XmlAttribute() {
    }

    /**
     * 获得属性
     *
     * @return 获得属性的字段类型
     */
    public AttributeType getAttributeType() {
        return attributeType;
    }

    /**
     * 设置属性的数据类型
     *
     * @param attributeType 数据类型
     */
    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }
}
