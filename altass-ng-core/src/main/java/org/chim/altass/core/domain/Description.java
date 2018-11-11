/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.node.domain
 * Author: Xuejia
 * Date Time: 2016/12/15 11:02
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;

/**
 * Class Name: Description
 * Create Date: 2016/12/15 11:02
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * xml描述节点
 */
@Elem(alias = "description")
public class Description extends XmlNode {

    private static final long serialVersionUID = 500465404029745846L;
    /**
     * 描述节点的内容
     */
    @Attr(alias = "text")
    private String text = null;

    public Description() {
    }

    public Description(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
