/**
 * Project: x-framework
 * Package Name: org.ike.monitor.support.ssh
 * Author: Xuejia
 * Date Time: 2016/10/10 7:37
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.toolkit.ssh;

/**
 * Class Name: ChannelType
 * Create Date: 2016/10/10 7:37
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: ChannelType枚举
 */
public enum ChannelType {
    SHELL("shell"),
    EXEC("exec");

    private String channelType = null;

    ChannelType(String ct) {
        this.channelType = ct;
    }

    @Override
    public String toString() {
        return this.channelType;
    }
}
