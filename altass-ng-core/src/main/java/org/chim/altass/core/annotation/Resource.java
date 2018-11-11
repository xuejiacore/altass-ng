package org.chim.altass.core.annotation;

import java.lang.annotation.*;

/**
 * Class Name: Resource
 * Create Date: 2017/9/15 18:59
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Resource {

    /**
     * 对应需要装配的元素，默认为Entry
     *
     * @return 执行节点需要装配的元素，默认为Entry
     */
    String entry() default "org.chim.altass.domain.buildin.entry.Entry";

    /**
     * 装配时默认的节点大图标路径
     *
     * @return 节点大图标路径
     */
    String bigImage() default "res/image/support/nodeflow/bgs/default.png";

    /**
     * 装配时默认的节点中图标路径
     *
     * @return 节点中图标路径
     */
    String midImage() default "res/image/support/nodeflow/bgs/default.png";

    /**
     * 装配时默认的节点小图标路径
     *
     * @return 节点小图标路径
     */
    String smallImage() default "res/image/support/nodeflow/bgs/default.png";

    /**
     * 装配的节点名称
     *
     * @return 节点名称
     */
    String name() default "未定义";

    /**
     * 装配的节点分组名称
     *
     * @return 节点分组名称
     */
    String groupName() default "default";

    /**
     * 节点的配置页面
     *
     * @return 配置页面的url
     */
    String pageUrl() default "http://example.foo.com";

    /**
     * 节点的运行执行器类名
     *
     * @return 执行器类名
     */
    Class<?> clazz();
}
