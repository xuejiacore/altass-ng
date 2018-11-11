/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml.domain
 * Author: Xuejia
 * Date Time: 2016/12/10 18:40
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: XmlBinding
 * Create Date: 2016/12/10 18:40
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class XmlBinding {
    private String start = null;
    private Map<String, XmlMapping> mappings = null;

    public XmlBinding() {
    }

    public void addMapping(String name, XmlMapping mapping) {
        if (this.mappings == null) {
            this.mappings = new HashMap<>();
        }
        this.mappings.put(name, mapping);
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public Map<String, XmlMapping> getMappings() {
        return this.mappings = this.mappings == null ? new HashMap<>() : this.mappings;
    }

    public void setMappings(Map<String, XmlMapping> mappings) {
        this.mappings = mappings;
    }
}
