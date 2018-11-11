package org.chim.altass.toolkit;

import org.chim.altass.base.cache.BasicRedisKey;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.io.Serializable;
import java.util.Set;

/**
 * Class Name: JedisToolkit
 * Create Date: 18-3-9 下午2:04
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class JedisToolkit {

    private static JedisToolkit toolkit = null;


    private JedisToolkit() {
    }

    public static JedisToolkit getInstance() {
        if (toolkit == null) {
            synchronized (JedisToolkit.class) {
                if (toolkit == null) {
                    toolkit = new JedisToolkit();
                }
            }
        }
        return toolkit;
    }

    public static boolean set() {
        return false;
    }

    public boolean zadd(BasicRedisKey key, Serializable id, Double score, String member) {
        Jedis jedis = null;
        try {
            jedis = connect();
            Long len = jedis.zadd(buildKey(key, id), score, member);
            return len == 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.disconnect();
            }
        }
        return false;
    }

    public boolean zrem(BasicRedisKey key, Serializable id, String... members) {
        Jedis jedis = null;
        try {
            jedis = connect();
            Long len = jedis.zrem(buildKey(key, id), members);
            return len == 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.disconnect();
            }
        }
        return false;
    }

    /**
     * 获得按分数升序排列的数据
     *
     * @param key   缓存key
     * @param id    key id
     * @param start 开始
     * @param end   结束
     * @return LinkedHashSet
     */
    public Set<Tuple> zrange(BasicRedisKey key, Serializable id, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = connect();
            return jedis.zrangeWithScores(buildKey(key, id), start, end);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.disconnect();
            }
        }
        return null;
    }

    /**
     * 构建
     *
     * @param key 需要构建的key
     * @param id  key id
     * @return 构建完成的key值
     */
    public static String buildKey(BasicRedisKey key, Serializable id) {
        return key.redisKey() + BasicRedisKey.KEY_ID_SEPARATOR + id;
    }

    private Jedis connect() {
        return new Jedis("127.0.0.1", 6379);
    }

}
