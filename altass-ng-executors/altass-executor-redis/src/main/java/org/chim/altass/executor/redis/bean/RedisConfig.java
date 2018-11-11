package org.chim.altass.executor.redis.bean;

/**
 * Class Name: RedisConfig
 * Create Date: 11/5/18 11:18 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class RedisConfig {
    private String serverGroup = "default";
    private String host = "127.0.0.1";
    private int port = 6379;
    private int timeout = 2000;

    private Integer maxActive = 40;
    private Integer maxIdle = 10;
    private Long maxWait = 1000L;

    public RedisConfig() {
    }

    public RedisConfig(String serverGroup, String host, int port) {
        this.serverGroup = serverGroup;
        this.host = host;
        this.port = port;
    }

    public RedisConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getServerGroup() {
        return serverGroup;
    }

    public void setServerGroup(String serverGroup) {
        this.serverGroup = serverGroup;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Long maxWait) {
        this.maxWait = maxWait;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
