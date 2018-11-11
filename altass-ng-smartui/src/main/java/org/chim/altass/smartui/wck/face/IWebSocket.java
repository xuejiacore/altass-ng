/**
 * Project: x-framework
 * Package Name: org.ike.monitor.websockets
 * Author: Xuejia
 * Date Time: 2016/10/10 13:30
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.wck.face;

import javax.websocket.Session;

/**
 * Class Name: IWebSocket
 * Create Date: 2016/10/10 13:30
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface IWebSocket {
    /**
     * 打开链接
     *
     * @param session 当前打开链接的session
     */
    void onOpen(Session session);

    /**
     * 关闭连接
     */
    void onClose();

    /**
     * 连接异常
     *
     * @param session 当前的会话
     * @param err     发生的异常
     */
    void onError(Session session, Throwable err);

    /**
     * 接收消息
     *
     * @param message 发送的消息
     * @param session 当前会话
     */
    void onMessage(String message, Session session);
}
