/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml.domain
 * Author: Xuejia
 * Date Time: 2016/12/10 18:37
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: XmlMapping
 * Create Date: 2016/12/10 18:37
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 可Xml化映射
 */
public class XmlMapping extends XmlNode {
    private String clazz = null;
    private List<XmlNamespace> namespaces = null;
    private List<XmlAttribute> attributes = null;
    private List<XmlElement> elements = null;
    private String textField = null;
    private String version = null;

    public XmlMapping() {
    }

    public void addElement(XmlElement element) {
        if (this.elements == null) {
            this.elements = new ArrayList<>();
        }
        this.elements.add(element);
    }

    public void addAttribute(XmlAttribute attribute) {
        if (this.attributes == null) {
            this.attributes = new ArrayList<>();
        }
        this.attributes.add(attribute);
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<XmlNamespace> getNamespaces() {
        return this.namespaces = this.namespaces == null ? new ArrayList<>() : namespaces;
    }

    public void setNamespaces(List<XmlNamespace> namespaces) {
        this.namespaces = namespaces;
    }

    public List<XmlAttribute> getAttributes() {
        return this.attributes = this.attributes == null ? new ArrayList<>() : attributes;
    }

    public void setAttributes(List<XmlAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<XmlElement> getElements() {
        return this.elements = this.elements == null ? new ArrayList<>() : elements;
    }

    public void setElements(List<XmlElement> elements) {
        this.elements = elements;
    }

    public String getTextField() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField = textField;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
