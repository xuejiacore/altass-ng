package org.chim.altass.executor.redis.support;

import org.chim.altass.executor.redis.bean.Param;
import org.chim.altass.executor.redis.bean.RedisConfig;
import org.chim.altass.executor.redis.bean.IRedisKey;
import org.chim.altass.executor.redis.bean.Response;
import org.chim.altass.executor.redis.constant.OpType;
import org.chim.altass.executor.redis.exception.RedisException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class Name: JedisCmd
 * Create Date: 11/5/18 11:17 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public abstract class AbstractJedisCmd {

    protected boolean compatible = false;

    private Random random = new Random();

    private Map<String, List<JedisPool>> poolMap = new ConcurrentHashMap<String, List<JedisPool>>();

    public AbstractJedisCmd(RedisConfig... configs) {
        this.initConnectConf(configs);
    }

    private void initConnectConf(RedisConfig... configs) {
        for (RedisConfig config : configs) {
            // group config
            String group = config.getServerGroup();
            List<JedisPool> poolList = poolMap.computeIfAbsent(group, k -> new ArrayList<JedisPool>());

            JedisPoolConfig poolConfig = new JedisPoolConfig();
//            poolConfig.setMaxIdle(config.getMaxIdle());
//            poolConfig.setMaxTotal(config.getMaxActive());
//            poolConfig.setMaxWaitMillis(config.getMaxWait());
            poolList.add(new JedisPool(poolConfig, config.getHost(), config.getPort(), config.getTimeout()));
        }
    }

    private JedisPool getPool(IRedisKey key) {
        String group = key.serverGroup();
        group = group == null ? "default" : group;

        List<JedisPool> jedisPools = poolMap.get(group);
        if (jedisPools == null) {
            return null;
        }
        int hit = random.nextInt(jedisPools.size());
        return jedisPools.get(hit);
    }

    private JedisPool getPool(String group) {
        List<JedisPool> jedisPools = poolMap.get(group);
        if (jedisPools == null) {
            return null;
        }
        int hit = random.nextInt(jedisPools.size());
        return jedisPools.get(hit);
    }

    public <T> Response<T> query(IRedisKey key, Serializable id, Operator<T> operator, Object... values) {
        Param param = new Param(key, id, values);
        param.setOp(OpType.QUERY);
        return execute(param, operator);
    }

    public <T> Response<T> execute(Param param, Operator<T> operator) {

        // Parameter Checker
        JedisPool pool = getPool(param.getRedisKey());
        try {
            // Change expires if Change
            T executeResult = executeWithPool(pool, param, operator);
            return new Response<T>(executeResult);
        } catch (Throwable e) {
            throw new RedisException(e);
        }
    }

    private <T> T executeWithPool(JedisPool pool, Param param, Operator<T> operator) {
        final Jedis[] jedis = {null};
        int index = 0;

        int retryTimes = 3;
        for (; index < retryTimes; index++) {
            try {
                jedis[0] = pool.getResource();
                if (jedis[0] == null) {
                    throw new IllegalArgumentException("Could not obtain jedis instance from jedis pool.");
                }
                T execResult = operator.exec(jedis[0], param);

                if (param.getRedisKey().expire() > 0 && OpType.CHANGE.equals(param.getOp())) {
                    operator.expireKey(jedis[0], param);
                }
                return execResult;
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                if (jedis[0] != null) {
                    jedis[0].close();
                }
            }
        }

        if (index == retryTimes) {
            throw new JedisConnectionException("Could not connect redis server after retry " + retryTimes + " times.");
        }

        return null;
    }
}
