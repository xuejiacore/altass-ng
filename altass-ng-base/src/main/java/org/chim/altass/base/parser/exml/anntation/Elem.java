package org.chim.altass.base.parser.exml.anntation;

import java.lang.annotation.*;

/**
 * Class Name: Element
 * Create Date: 17-12-10 下午11:46
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Documented
@Inherited
public @interface Elem {
    /**
     * 定义xml节点的别名
     *
     * @return 节点别名
     */
    String alias();

    /**
     * 定义xml节点的描述信息
     *
     * @return 节点的描述
     */
    String desc() default "";

    /**
     * 如果该节点是一个接口类型的容器类型，那么需要知名是可接口化的
     *
     * @return 如果可以接口化，那么定义为true，否则定义为false
     */
    boolean interfacing() default false;

    /**
     * 当前文档解析使用的版本号，默认1.0.0
     *
     * @return 使用的解析器版本类型
     */
    String version() default "1.0.0";

    /**
     * 解析优先级
     *
     * @return 解析优先值越小越优先解析
     */
    int priority() default Integer.MAX_VALUE;
}
