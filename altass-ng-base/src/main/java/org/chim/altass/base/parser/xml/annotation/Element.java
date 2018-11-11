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
 * Class Name: Element
 * Create Date: 2016/12/10 18:31
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 注解一个字段或者是类为元素
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Element {
    /**
     * 节点名称，解析为xml节点的标签名称
     *
     * @return 节点名称
     */
    String name();

    /**
     * 节点的版本号
     *
     * @return 节点的版本号，该版本号仅在文档记录，不做运行时处理，默认为Beta v0.0.1
     */
    String version() default "Beta v0.0.1";

    /**
     * 是否是可组装的，默认为不可组装
     *
     * @return 节点可组装，那么返回值为true，否则返回值为false
     */
    boolean assemble() default false;

    /**
     * 描述
     *
     * @return 描述信息
     */
    String desc() default "undefined";
}
