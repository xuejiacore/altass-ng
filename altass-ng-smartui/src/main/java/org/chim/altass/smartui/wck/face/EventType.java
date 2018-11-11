/**
 * Project: x-framework
 * Package Name: org.ike.monitor.websockets.face
 * Author: Xuejia
 * Date Time: 2016/10/10 13:41
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.wck.face;

/**
 * Class Name: EventType
 * Create Date: 2016/10/10 13:41
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public enum EventType {

    // =================================================
    USER(0x1000),
    /**
     * 登陆事件
     */
    LOGGIN_IN(USER.value() | 0x1),
    /**
     * 登出事件
     */
    LOGGIN_OUT(USER.value() | 0x2),


    // =================================================
    SYSTEM(0x2000),
    /**
     * 系统标准信息
     */
    INFO(SYSTEM.value() | 0x01),
    /**
     * 系统警告信息
     */
    WARNING(SYSTEM.value() | 0x02),
    /**
     * 系统错误信息
     */
    ERR(SYSTEM.value() | 0X04),
    /**
     * 初始化成功
     */
    INIT_SUCCESS(SYSTEM.value() | 0x08),
    /**
     * Email
     */
    EMAIL(SYSTEM.value() | 0x10),
    /**
     * 提示信息
     */
    TIPS(SYSTEM.value() | 0x20);
    // ————————————————————————————————————————————————————————


    // ————————————————————————————————————————————————————————
    private int eventCode = 0;

    EventType(int eventCode) {
        this.eventCode = eventCode;
    }

    public int value() {
        return this.eventCode;
    }

    /**
     * 系统行为事件
     */
    interface System {
    }
}
