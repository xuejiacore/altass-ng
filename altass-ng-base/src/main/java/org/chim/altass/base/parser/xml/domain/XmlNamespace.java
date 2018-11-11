/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml.domain
 * Author: Xuejia
 * Date Time: 2016/12/10 18:33
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml.domain;

/**
 * Class Name: XmlNamespace
 * Create Date: 2016/12/10 18:33
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 可Xml化命名空间节点
 */
public class XmlNamespace extends XmlNode {
    private String prefix = null;
    private String uri = null;

    public XmlNamespace(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }

    public XmlNamespace() {
        this(null, null);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
