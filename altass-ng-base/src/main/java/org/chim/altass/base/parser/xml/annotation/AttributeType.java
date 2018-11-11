/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml.annotation
 * Author: Xuejia
 * Date Time: 2016/12/12 11:47
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml.annotation;

/**
 * Class Name: AttributeType
 * Create Date: 2016/12/12 11:47
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 元素属性的数据类型
 */
public enum AttributeType {
    /**
     * 字符串类型
     */
    TEXT,
    /**
     * 数字类型
     */
    NUMERIC,
    /**
     * 正数类型
     */
    INT,
    /**
     * 日期类型
     */
    DATE,
    /**
     * 集合类型
     */
    COLLECTION,
    MAP
}
