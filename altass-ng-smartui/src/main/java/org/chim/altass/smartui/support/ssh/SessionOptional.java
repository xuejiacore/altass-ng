/**
 * Project: x-framework
 * Package Name: org.ike.monitor.support.ssh
 * Author: Xuejia
 * Date Time: 2016/10/10 8:43
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.support.ssh;

/**
 * Class Name: SessionOptional
 * Create Date: 2016/10/10 8:43
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:Session的可选配置项
 */
public enum SessionOptional {
    /**
     * 第一次访问服务器密钥配置
     */
    K_STRICT_HOST_KEY_CHECKING("StrictHostKeyChecking"),
    /**
     * 第一次访问服务器默认的密钥配置：不需要使用密钥
     */
    V_DEFAULT_STRICT_HOST_KEY_CHECKING("no");

    private String key = null;

    SessionOptional(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
