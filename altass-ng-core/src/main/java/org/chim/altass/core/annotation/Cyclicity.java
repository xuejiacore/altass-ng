package org.chim.altass.core.annotation;

import java.lang.annotation.*;

/**
 * Class Name: DAG
 * Create Date: 2017/10/19 12:41
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Cyclicity
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cyclicity {

    /**
     * 是否允许环的出现，默认不允许出现有向循环
     *
     * @return 如果允许环出现，值为1，否则值为0
     */
    boolean recyclable() default false;
}
