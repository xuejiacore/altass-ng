/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml.annotation
 * Author: Xuejia
 * Date Time: 2016/12/10 21:41
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class Name: Text
 * Create Date: 2016/12/10 21:41
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 注解一个字段是文本
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Text {
}
