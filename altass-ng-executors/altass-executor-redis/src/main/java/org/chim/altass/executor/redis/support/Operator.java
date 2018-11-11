package org.chim.altass.executor.redis.support;

import org.chim.altass.executor.redis.bean.Param;
import redis.clients.jedis.Jedis;

/**
 * Class Name: Operator
 * Create Date: 11/5/18 11:04 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public abstract class Operator<T> {
    /**
     * To execute a redis request
     *
     * @param jedis jedis
     * @param param Redis parameter
     * @return return the redis response
     */
    public abstract T exec(Jedis jedis, Param param);

    /**
     * set a timeout on the specified key.
     *
     * @param jedis jedis
     * @param param Redis parameter
     * @return return true when timeout was set.
     */
    public boolean expireKey(Jedis jedis, Param param) {

        try {
            // return true when the timeout was set.
            return 1 == jedis.expire(param.buildKey(), param.getRedisKey().expire());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }
}
