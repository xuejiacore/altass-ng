/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml.domain
 * Author: Xuejia
 * Date Time: 2016/12/10 18:32
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml.domain;

/**
 * Class Name: XmlNode
 * Create Date: 2016/12/10 18:32
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 可Xml解析节点
 */
public class XmlNode {
    private String name = null;
    private String field = null;

    public XmlNode() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
