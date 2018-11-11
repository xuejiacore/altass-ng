package org.chim.altass.executor.flume.bean;

/**
 * Class Name: FlumeConfig
 * Create Date: 11/10/18 8:41 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class FlumeConfig {

    // 通信token，防止其他消息入队
    private String token = null;

    // 数据接入的uuid
    private String uuid = null;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
