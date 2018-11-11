/**
 * Project: x-framework
 * Package Name: org.ike.monitor.websockets
 * Author: Xuejia
 * Date Time: 2016/10/10 13:29
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.wck.commons;

import org.chim.altass.smartui.wck.face.EventType;
import org.chim.altass.smartui.wck.face.IEventListener;
import org.chim.altass.smartui.wck.face.IWebSocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class Name: SktNotification
 * Create Date: 2016/10/10 13:29
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: 接受通知类的WebSocket
 * 该WebSocket用于监听通知类型的操作，在触发了相应的事件后能够发送数据到前台进行数据交互
 */
@ServerEndpoint("/notification")
public class SktNotification implements IWebSocket, IEventListener, NotificationInitialization.IChecker {
    private static CopyOnWriteArrayList<SktNotification> webSockets = new CopyOnWriteArrayList<SktNotification>();
    private Session session = null;

    /**
     * @param session 当前打开链接的session
     */
    @OnOpen
    @Override
    public void onOpen(Session session) {
        this.session = session;
        webSockets.add(this);

        /*
         * 当打开一个页面后，检查相关的内容，
         */
        NotificationInitialization initialization = new NotificationInitialization();
        initialization.bindingEventListener(this);
        initialization.check(this);
    }

    @OnClose
    @Override
    public void onClose() {
        webSockets.remove(this);
        System.out.println("关闭当前的连接");
    }

    @OnError
    @Override
    public void onError(Session session, Throwable err) {
    }

    @OnMessage
    @Override
    public void onMessage(String message, Session session) {
        // TODO:接收前台发送过来的数据，处理后能够进行广播
        // 此处场景目前应用于某个用户登录后检查
    }

    @Override
    public void onEvent(EventType event, Object data, String tag, Object... args) {
        // TODO：触发了一个事件后，将相应的数据内容发送到前端进行处理
        if (sendData(packageData(event, data, tag, args))) {

        }
    }

    /**
     * 打包数据
     *
     * @param event 事件的类型
     * @param data  产生的数据
     * @param tag   标记
     * @param args  事件参数
     * @return 将特定的数据类型打包成一个字符串类型的数据
     */
    private String packageData(EventType event, Object data, String tag, Object[] args) {
        return (String) data;
    }

    /**
     * 将数据发送到前台
     *
     * @param data 需要发送的数据
     * @return 如果数据发送成功，那么返回值为true，否则返回值为false
     */
    private boolean sendData(Object data) {
        try {
            session.getBasicRemote().sendText((String) data);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void onChecking(IEventListener eventListener) {
         /*
            TODO:将该对象赋予另外一个处理程序，允许另外一个处理程序使用eventListener来触发通知类消息
         */

        /*
            TODO:做一些检测工作，检测完成后对需要通知的对象进行通知
         */
        if (eventListener != null) {
            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                eventListener.onTrigger(EventType.INFO, "普通的提示信息", null);
            }).start();
        }
    }
}