/*
 * Project: x-framework
 * Package Name: org.ike.etl.config
 * Author: Xuejia
 * Date Time: 2017/2/7 9:50
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.configuration;

import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.config.Config;

/**
 * Class Name: RedissonConfig
 * Create Date: 2017/2/7 9:50
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public abstract class RedissonConfig {
    protected Config config = null;
    protected RedissonClient client = null;
    protected RedisClient redisClient = null;

    public RedissonConfig() {
        this.config = new Config();
    }

    public RedissonConfig(Config config) {
        this.config = config;
    }

    public RedisClient getRedisClient() {
        return redisClient;
    }

    public void setRedisClient(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    public abstract RedissonClient create();
}
