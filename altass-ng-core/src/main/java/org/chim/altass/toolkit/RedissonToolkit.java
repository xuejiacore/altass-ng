package org.chim.altass.toolkit;

import org.chim.altass.base.cache.BasicRedisKey;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.client.RedisClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Class Name: RedissonTool
 * Create Date: 17-12-27 下午9:13
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class RedissonToolkit {

    private static RedissonClient redissonClient = null;

    private static RedisClient redisClient = null;

    private static final String REDIS_HOST_PORT = "127.0.0.1:6379";

    private static RedissonToolkit toolkit = null;

    /**
     * 获得缓存操作工具箱实例
     *
     * @return 缓存操作工具箱实例
     */
    public static RedissonToolkit getInstance() {
        if (toolkit == null) {
            synchronized (RedissonToolkit.class) {
                if (toolkit == null) {
                    toolkit = new RedissonToolkit();
                }
            }
        }
        return toolkit;
    }

    public static RedissonToolkit getInstance(Config config) {
        if (toolkit == null) {
            synchronized (RedissonToolkit.class) {
                if (toolkit == null) {
                    toolkit = new RedissonToolkit(config);
                }
            }
        }
        return toolkit;
    }

    public static RedissonToolkit getInstance(RedissonClient redissonClient, RedisClient redisClient) {
        if (toolkit == null) {
            synchronized (RedissonToolkit.class) {
                if (toolkit == null) {
                    toolkit = new RedissonToolkit(redissonClient, redisClient);
                }
            }
        }
        return toolkit;
    }

    public void delete() {
    }

    public RCountDownLatch getCountDownLatch(BasicRedisKey key, Serializable id, long count) {
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(RedissonToolkit.buildKey(key, id));
        countDownLatch.trySetCount(count);
        return countDownLatch;
    }

    public RCountDownLatch getCountDownLatch(BasicRedisKey key, Serializable id) {
        return redissonClient.getCountDownLatch(RedissonToolkit.buildKey(key, id));
    }

    /**
     * 构建缓存操作工具箱实例
     */
    private RedissonToolkit() {
        Config config = new Config();
        config.useSingleServer().setAddress(REDIS_HOST_PORT);
        RedissonToolkit.redissonClient = Redisson.create(config);
        RedissonToolkit.redisClient = new RedisClient(REDIS_HOST_PORT);
    }

    private RedissonToolkit(RedissonClient redissonClient, RedisClient redisClient) {
        RedissonToolkit.redissonClient = redissonClient;
        RedissonToolkit.redisClient = redisClient;
    }

    /**
     * 构建缓存操作工具箱实例
     *
     * @param config 缓存连接配置
     */
    private RedissonToolkit(Config config) {
        RedissonToolkit.redissonClient = Redisson.create(config);
        RedissonToolkit.redisClient = new RedisClient(REDIS_HOST_PORT);
    }

    public RedisClient getRedisClient() {
        return redisClient;
    }

    /**
     * 发布一个消息
     *
     * @param channel channel
     * @param msg     需要发布的消息
     * @return 订阅端接收到数据的数量
     */
    public Long publish(String channel, Object msg) {
        return redissonClient.getTopic(channel, new StringCodec()).publish(msg);
    }
    // ----

    /**
     * 获得一个分布式链表数据结构对象实例
     *
     * @param key 键
     * @return 分布式数据结构对象实例
     */
    public RList<Object> getList(BasicRedisKey key, Serializable id) {
        String redisKey = buildKey(key, id);
        return redissonClient.getList(redisKey);
    }

    /**
     * 允许进行List存储操作
     *
     * @param key  redis key
     * @param list 列表
     * @return 返回缓存中的数据
     */
    public RList<Object> list(BasicRedisKey key, Serializable id, List<Object> list) {
        RList<Object> cacheData = getList(key, id);
        if (list != null) {
            cacheData.addAll(list);
        }
        return cacheData;
    }

    /**
     * 获得一个分布式Map数据结构对象实例
     *
     * @param key 键
     * @return 分布式Map数据结构对象实例
     */
    public RMap getMap(BasicRedisKey key, Serializable id) {
        return redissonClient.getMap(buildKey(key, id));
    }

    /**
     * 获得bucket中的对象
     *
     * @param key 对象对应的key
     * @param id  key id
     * @return 获得redis中的缓存对象
     */
    public Object getBucket(BasicRedisKey key, Serializable id) {
        RBucket<Object> bucket = redissonClient.getBucket(buildKey(key, id));
        if (bucket != null) {
            return bucket.get();
        }
        return null;
    }

    public RBucket getRBucket(BasicRedisKey key, Serializable id) {
        return redissonClient.getBucket(buildKey(key, id));
    }

    public RBucket getRBucket(BasicRedisKey key, Serializable id, Object def) {
        RBucket bucket = redissonClient.getBucket(buildKey(key, id));
        if (bucket != null) {
            Object data = bucket.get();
            if (data == null) {
                bucket = setBucket(key, id, def);
                return bucket;
            }
        }
        return null;
    }

    public Object getBucket(BasicRedisKey key, Serializable id, Object def) {
        RBucket bucket = redissonClient.getBucket(buildKey(key, id));
        if (bucket != null) {
            Object data = bucket.get();
            if (data == null) {
                bucket = setBucket(key, id, def);
                return bucket.get();
            }
            return data;
        }
        return null;
    }

    /**
     * 设置一个对象
     *
     * @param key  key
     * @param id   key id
     * @param data 数据对象
     */
    public RBucket setBucket(BasicRedisKey key, Serializable id, Object data) {
        RBucket<Object> bucket = redissonClient.getBucket(buildKey(key, id));
        bucket.set(data);
        return bucket;
    }

    /**
     * 设置超时时长
     *
     * @param expirable 可以设置超时时长的对象
     * @param key       对应的key定义，包含有ttl设置
     */
    public void expire(RExpirable expirable, BasicRedisKey key) {
        expirable.expire(key.ttl(), TimeUnit.MILLISECONDS);
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

    public RLock getLock(BasicRedisKey key, Serializable id) {
        return redissonClient.getLock(buildKey(key, id));
    }
}
