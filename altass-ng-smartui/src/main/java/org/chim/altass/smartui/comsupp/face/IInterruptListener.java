/**
 * Project: x-framework
 * Package Name: org.ike.monitor.comsupp.face
 * Author: Xuejia
 * Date Time: 2016/10/9 13:04
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.comsupp.face;

/**
 * Class Name: IInterruptListener
 * Create Date: 2016/10/9 13:04
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: 终端信号监听器
 */
public interface IInterruptListener {
    /**
     * 终端信号
     *
     * @return 中断正常，那么返回值为true，否则返回值为false
     */
    boolean onInterrupt(Object data);
}
