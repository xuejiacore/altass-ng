package org.chim.altass.base.parser.exml.anntation;

import java.lang.annotation.*;

/**
 * Class Name: Attr
 * Create Date: 17-12-10 下午11:47
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 定义节点解析的属性类型
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Documented
@Inherited
public @interface Attr {

    /**
     * 定义属性的名称
     *
     * @return 属性的名称
     */
    String alias();

    /**
     * 属性的类型
     *
     * @return 需要解析的属性的类型，目前仅支持基本类型
     */
    Type type() default Type.BASIC;

    /**
     * 解析优先级
     *
     * @return 优先级值越低，越优先解析
     */
    int priority() default Integer.MAX_VALUE;
}
