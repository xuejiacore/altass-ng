/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.centers.log
 * Author: Xuejia
 * Date Time: 2017/1/4 23:30
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.log;

/**
 * Class Name: ILogger
 * Create Date: 2017/1/4 23:30
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface ILogger {
    /**
     * 输出正常信息
     *
     * @param msg 需要打印的信息
     */
    void info(String msg);

    /**
     * 输出错误信息
     *
     * @param msg 错误日志信息
     */
    void error(String msg);
}
