package org.chim.altass.core.annotation;


import org.chim.altass.core.domain.buildin.attr.ACommon;

import java.lang.annotation.*;

/**
 * Class Name: Autowire
 * Create Date: 2017/10/18 17:57
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 自动使用指定的entry属性中的数据填充 Executor 实现类中的实体字段
 * <p>
 * 注意与 Spring 中的 Autowire 进行区分
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RuntimeAutowired {
    /**
     * 属性值是否是必要的
     */
    boolean required() default true;

    /**
     * 自动装配的数据源
     */
    Class source() default ACommon.class;

    /**
     * 是否需要参数解析
     *
     * @return 是否需要参数解析，默认解析
     */
    boolean analyzable() default true;
}
