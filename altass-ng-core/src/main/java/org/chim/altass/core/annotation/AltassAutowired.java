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
 * To annotate a var that could be autowired with altass runtime injection. See also
 * {@link org.chim.altass.core.executor.AbstractExecutor} and {@link ACommon}
 * <p>
 * The executor will auto assemble the value which was annotated with this annotation.
 * {@link org.chim.altass.core.domain.buildin.entry.Entry#inject(String, Object)} or
 * {@link org.chim.altass.core.domain.buildin.entry.Entry#inject(Class, String)} and other useful method.
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AltassAutowired {

    /**
     * Indicate the value is required. Default is true.
     */
    boolean required() default true;

    /**
     * Default data source class. Default is {@link ACommon}
     */
    Class source() default ACommon.class;

    /**
     * Indicate the value could be parse with build-in parameter expression analyzer. Default is true
     */
    boolean analyzable() default true;
}
