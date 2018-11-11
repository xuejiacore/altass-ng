/**
 * Project: x-framework
 * Package Name: org.ike.monitor.websockets.commons
 * Author: Xuejia
 * Date Time: 2016/10/10 14:17
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.wck.commons;

import org.chim.altass.smartui.wck.face.IEventListener;

/**
 * Class Name: NotificationInitialization
 * Create Date: 2016/10/10 14:17
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: 通知类初始化
 */
public class NotificationInitialization {
    private IEventListener eventListener = null;
    private IChecker checker = null;

    public interface IChecker {
        void onChecking(IEventListener eventListener);
    }

    public NotificationInitialization() {
        System.err.println("正在进行通知类的初始化工作");
    }

    public void bindingEventListener(IEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * 初始化完成后执行相关的检查，将检查后的结果回调给观察者
     */
    public void check(IChecker checker) {
        if (checker != null) {
            checker.onChecking(this.eventListener);
        }
    }
}
