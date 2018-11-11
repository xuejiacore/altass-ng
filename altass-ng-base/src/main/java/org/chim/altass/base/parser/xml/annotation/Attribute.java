/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml.annotation
 * Author: Xuejia
 * Date Time: 2016/12/10 18:31
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class Name: Attribute
 * Create Date: 2016/12/10 18:31
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 注解一个字段是属性字段
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Attribute {
    /**
     * 属性的名称
     *
     * @return 属性的名称
     */
    String name();

    /**
     * 属性的类型，默认为字符串类型
     *
     * @return 属性的类型枚举
     */
    AttributeType type() default AttributeType.TEXT;

    /**
     * 描述
     *
     * @return 描述信息
     */
    String desc() default "undefined";
}
