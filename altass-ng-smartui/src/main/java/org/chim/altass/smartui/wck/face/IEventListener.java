/**
 * Project: x-framework
 * Package Name: org.ike.monitor.websockets.face
 * Author: Xuejia
 * Date Time: 2016/10/10 13:39
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.wck.face;

/**
 * Class Name: IEventListener
 * Create Date: 2016/10/10 13:39
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * 事件类监听器
 */
public interface IEventListener extends INotificationListener {

    /**
     * 触发事件类通知
     *
     * @param event 事件的标志
     * @param data  数据内容
     * @param tag   标记
     * @param args  参数
     */
    @Override
    default void onTrigger(EventType event, Object data, String tag, Object... args) {
        onEvent((EventType) event, data, tag, args);
    }

    /**
     * 接收并处理通知
     *
     * @param event 事件类型
     * @param data  事件的数据
     * @param tag   事件标记
     * @param args  时间的其他参数
     */
    void onEvent(EventType event, Object data, String tag, Object... args);
}
