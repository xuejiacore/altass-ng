/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.node.annotation
 * Author: Xuejia
 * Date Time: 2016/12/27 15:00
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.annotation;

import java.lang.annotation.*;

/**
 * Class Name: Executable
 * Create Date: 2016/12/27 15:00
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Executable {

    /**
     * 执行节点需要装配的名称
     *
     * @return 装配名称
     */
    String name();

    /**
     * 是否是可组装的，默认为不可组装
     *
     * @return 节点可组装，那么返回值为true，否则返回值为false
     */
    boolean assemble() default false;

    /**
     * 节点能力
     *
     * @return 节点能力列表
     */
    String[] ability() default {};
}
