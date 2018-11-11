/*
 * Project: x-framework
 * Package Name: org.ike.etl.core.domain
 * Author: Xuejia
 * Date Time: 2017/2/15 10:21
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.domain;


import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.core.constant.Status;
import org.chim.altass.toolkit.RedissonToolkit;

/**
 * Class Name: Status
 * Create Date: 2017/2/15 10:21
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class StatusInfo {

    public static final Integer STATUS_SKIPPED = 0x1;

    private IEntry entry = null;
    private Integer statusCode = Status.RUNNING.getStatusCode();
    private RedissonToolkit redissonToolkit = null;
    private String executeId = null;

    public StatusInfo(String executeId) {
        this.executeId = executeId;
        this.redissonToolkit = RedissonToolkit.getInstance();
        this.redissonToolkit.setBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_STATUS, executeId, statusCode);
    }

    public IEntry getEntry() {
        return entry;
    }

    public void setEntry(IEntry entry) {
        this.entry = entry;
    }

    public Integer getStatusCode() {
        this.statusCode = (Integer) this.redissonToolkit.getBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_STATUS, this.executeId);
        return this.statusCode;
    }

    public StatusInfo setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        this.redissonToolkit.setBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_STATUS, this.executeId, this.statusCode);
        return this;
    }

    @Override
    public String toString() {
        return "Status{" +
                "entry=" + entry +
                ", statusCode=" + statusCode +
                ", redissonToolkit=" + redissonToolkit +
                ", executeId='" + executeId + '\'' +
                '}';
    }
}
