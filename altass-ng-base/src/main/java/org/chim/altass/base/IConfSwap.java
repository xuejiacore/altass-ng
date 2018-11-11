/**
 * Project: x-framework
 * Package Name: org.ike
 * Author: Xuejia
 * Date Time: 2016/10/9 14:54
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base;

/**
 * Class Name: IConfSwap
 * Create Date: 2016/10/9 14:54
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: 配置热插拔接口
 */
public interface IConfSwap {

    /**
     * 重加载配置
     *
     * @return 返回重新加载的结果
     */
    boolean hotSwap();

    /**
     *
     */
    void register(String tag);
}
