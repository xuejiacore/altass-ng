/*
 * Project: x-framework
 * Package Name: org.ike.etl.config
 * Author: Xuejia
 * Date Time: 2017/2/7 9:55
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.config.SingleServerConfig;

/**
 * Class Name: SingleRedissonConfig
 * Create Date: 2017/2/7 9:55
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class SingleRedissonConfig extends RedissonConfig {

    private SingleServerConfig singleServerConfig = null;

    private String address = null;
    private String password = null;

    public SingleRedissonConfig() {
        singleServerConfig = config.useSingleServer();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        this.singleServerConfig.setAddress(address);
        this.redisClient = new RedisClient(address);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.singleServerConfig.setPassword(password);
    }

    @Override
    public RedissonClient create() {
        if (client == null) {
            synchronized (SingleRedissonConfig.class) {
                if (client == null) {
                    client = Redisson.create(this.config);
                }
            }
        }
        return client;
    }
}