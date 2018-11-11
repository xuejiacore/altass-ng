/**
 * Project: IkeChat
 * Package Name: org.ike.wechat.parser
 * Authorization: Xuejia
 * Date Time: 2016/6/13 15:29
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.network.parser;

/**
 * Class Name: IParameterKey
 * Create Date: 2016/6/13 15:29
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: 参数键值
 */
public interface IParameterKey extends IParameter {
    /**
     * 获取参数键
     *
     * @return 返回键名
     */
    String getKey();

    /**
     * 获得参数的描述
     *
     * @return 返回参数描述
     */
    String getDesc();
}
