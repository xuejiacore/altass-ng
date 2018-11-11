package org.chim.altass.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Class Name: Version
 * Create Date: 2017/10/4 13:28
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * specify a version
 * interface: client invoke version
 * server: service version
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Inherited
public @interface Version {

    /**
     * annotate interface's version
     *
     * @return version of interface
     */
    String version() default "default";
}
