/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.domain.event
 * Author: Xuejia
 * Date Time: 2016/12/15 18:03
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.event;


import org.chim.altass.base.parser.xml.annotation.Attribute;
import org.chim.altass.core.domain.Element;

/**
 * Class Name: MetaCallable
 * Create Date: 2016/12/15 18:03
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@org.chim.altass.base.parser.xml.annotation.Element(name = "call")
public class MetaCallable extends Element {
    @Attribute(name = "undefined")
    private String undefined = null;

    public String getUndefined() {
        return undefined;
    }

    public void setUndefined(String undefined) {
        this.undefined = undefined;
    }
}
