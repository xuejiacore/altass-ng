/**
 * Project: x-framework
 * Package Name: org.ike.monitor.websockets.face
 * Author: Xuejia
 * Date Time: 2016/10/10 13:39
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.wck.face;

/**
 * Class Name: INotificationListener
 * Create Date: 2016/10/10 13:39
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: 通知类事件监听器
 */
public interface INotificationListener {
    /**
     * 事件触发器
     *
     * @param event 事件的标志
     * @param data  数据内容
     * @param tag   标记
     * @param args  参数
     */
    void onTrigger(EventType event, Object data, String tag, Object... args);
}
