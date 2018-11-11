/**
 * Project: IkeChat
 * Package Name: org.ike.wechat.parser
 * Authorization: Xuejia
 * Date Time: 2016/6/13 15:29
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.network.parser;

/**
 * Class Name: IParameterValue
 * Create Date: 2016/6/13 15:29
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: 参数值
 */
public interface IParameterValue extends IParameter {
    /**
     * 获取参数值
     *
     * @return 参数值对象
     */
    Object getValue();
}
