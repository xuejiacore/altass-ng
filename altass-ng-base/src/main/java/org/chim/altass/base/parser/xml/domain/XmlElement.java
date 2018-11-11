/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml.domain
 * Author: Xuejia
 * Date Time: 2016/12/10 18:35
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml.domain;

/**
 * Class Name: XmlElement
 * Create Date: 2016/12/10 18:35
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 可Xml化节点元素
 */
public class XmlElement extends XmlNode {
    /**
     * 节点元素的类型
     */
    private String type = null;
    /**
     * 是否是集合类型
     */
    private boolean collection = false;

    public XmlElement() {
    }

    /**
     * 获得节点的元素类型
     *
     * @return 返回节点的元素类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置节点的元素类型
     *
     * @param type 节点的元素类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 是否是集合类型
     *
     * @return 如果是集合类型，返回值为true，否则返回值为false
     */
    public boolean isCollection() {
        return collection;
    }

    /**
     * 设置是否是集合类型
     *
     * @param collection 是否是集合类型
     */
    public void setCollection(boolean collection) {
        this.collection = collection;
    }
}
