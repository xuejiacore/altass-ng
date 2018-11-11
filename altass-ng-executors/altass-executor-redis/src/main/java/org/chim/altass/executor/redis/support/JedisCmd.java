package org.chim.altass.executor.redis.support;

import com.sun.istack.NotNull;
import org.chim.altass.executor.redis.bean.IRedisKey;
import org.chim.altass.executor.redis.bean.Param;
import org.chim.altass.executor.redis.bean.RedisConfig;
import org.chim.altass.executor.redis.bean.Response;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class Name: JedisCmd
 * Create Date: 11/5/18 11:24 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class JedisCmd extends AbstractJedisCmd {

    private static final Pattern KEY_EXPIRED_PATTERN = Pattern.compile("(.*)@(\\d+)$");

    public JedisCmd(RedisConfig... configs) {
        super(configs);
    }

    public JedisCmd(String host, int port) {
        super(new RedisConfig(host, port));
    }

    /**
     * 执行一个redis命令
     *
     * @param key    需要执行的key
     * @param id     key的id
     * @param params 执行参数
     * @return 执行接口
     */
    @SuppressWarnings("unchecked")
    public Object e(IRedisKey key, Serializable id, Object... params) {
        String c = key.cmd();
        if (c == null) {
            throw new IllegalArgumentException();
        }
        Cmd cmd = Cmd.valueOf(c.toUpperCase());
        return execute(new Param(key, id, params), cmd.getOperator()).getResult();
    }

    public Object e(String namespace, String cmd, String keyName, Serializable id, int expire, Object... params) {
        IRedisKey redisKey = new RedisKey(namespace, cmd, keyName, expire);
        return e(redisKey, id, params);
    }

    /**
     * 执行一个通用命令
     *
     * @param cmd     需要执行的redis命令
     * @param keyName key 的名称，支持携带超时时间，格式为 keynamefoo{200}，其中{200}是200s超时时间
     * @param id      key的id
     * @param params  执行命令所需的参数
     * @return 执行返回值
     */
    public Object e(@NotNull String cmd, @NotNull String keyName, @NotNull Serializable id, Object... params) {
        // 解析时间
        Matcher matcher = KEY_EXPIRED_PATTERN.matcher(keyName);
        int expire = 0;
        if (matcher.find()) {
            keyName = matcher.group(1);
            expire = Integer.valueOf(matcher.group(2));
        }
        IRedisKey redisKey = new RedisKey(cmd, keyName, expire);
        return e(redisKey, id, params);
    }

    public Object e(String cmd, String keyName, Serializable id, int expire, Object... params) {
        IRedisKey redisKey = new RedisKey(cmd, keyName, expire);
        return e(redisKey, id, params);
    }

    // =================================================================================================================
    //
    // =================================================================================================================

    public Boolean exists(IRedisKey key, Serializable id) {
        Response<Boolean> setResult = execute(new Param(key, id), Tool.exists());
        return setResult.getResult();
    }

    public Long expire(IRedisKey key, Serializable id, int seconds) {
        Response<Long> result = execute(new Param(key, id, seconds), Tool.expire());
        return result.getResult();
    }

    public Long TTL(IRedisKey key, Serializable id) {
        Response<Long> setResult = execute(new Param(key, id), Tool.ttl());
        return setResult.getResult();
    }

    public Long incr(IRedisKey key, Serializable id) {
        Response<Long> setResult = execute(new Param(key, id), Tool.incr());
        return setResult.getResult();
    }

    public Long incrBy(IRedisKey key, Serializable id, long value) {
        Response<Long> setResult = execute(new Param(key, id, value), Tool.incrBy());
        return setResult.getResult();
    }

    public Long decr(IRedisKey key, Serializable id) {
        Response<Long> setResult = execute(new Param(key, id), Tool.decr());
        return setResult.getResult();
    }

    public Long decrBy(IRedisKey key, Serializable id, long value) {
        Response<Long> setResult = execute(new Param(key, id, value), Tool.decrBy());
        return setResult.getResult();
    }

    public Long del(IRedisKey key, Serializable id) {
        Response<Long> setResult = execute(new Param(key, id), Tool.del());
        return setResult.getResult();
    }

    /**
     * set
     *
     * @param key key
     * @param id  key id
     * @param val set value
     * @return 如果操作成功，那么返回值为true，否则返回值为false
     */
    public boolean set(IRedisKey key, Serializable id, String val) {
        Response<String> setResult = execute(new Param(key, id, val), Tool.set());
        return "OK".equalsIgnoreCase(setResult.getResult());
    }

    /**
     * get
     *
     * @param key key
     * @param id  key id
     * @return 如果操作成功，那么获得key对应的值，否则返回值为null
     */
    public String get(IRedisKey key, Serializable id) {
        Response<String> result = execute(new Param(key, id), Tool.get());
        return result.getResult();
    }

    public String hget(IRedisKey key, Serializable id, String middleKey) {
        Response<String> result = execute(new Param(key, id, middleKey), Tool.hget());
        return result.getResult();
    }

    public String hmset(IRedisKey key, Serializable id, Map<String, String> map) {
        Response<String> result = execute(new Param(key, id, map), Tool.hmset());
        return result.getResult();
    }

    public Long hset(IRedisKey key, Serializable id, String middleKey, String value) {
        Response<Long> result = execute(new Param(key, id, middleKey, value), Tool.hset());
        return result.getResult();
    }

    public List<String> hmget(IRedisKey key, Serializable id, String... fields) {
        Response<List<String>> result = execute(new Param(key, id, fields), Tool.hmget());
        return result.getResult();
    }

    public Long hlen(IRedisKey key, Serializable id) {
        Response<Long> result = execute(new Param(key, id), Tool.hlen());
        return result.getResult();
    }

    public Long hdel(IRedisKey key, Serializable id, String... fields) {
        Response<Long> result = execute(new Param(key, id, fields), Tool.hdel());
        return result.getResult();
    }

    public Map<String, String> hgetAll(IRedisKey key, Serializable id) {
        Response<Map<String, String>> result = execute(new Param(key, id), Tool.hgetAll());
        return result.getResult();
    }

    public Boolean hexists(IRedisKey key, Serializable id, String field) {
        Response<Boolean> result = execute(new Param(key, id, field), Tool.hexists());
        return result.getResult();
    }

    public Long hincrby(IRedisKey key, Serializable id, String field, long value) {
        Response<Long> result = execute(new Param(key, id, field, value), Tool.hincrby());
        return result.getResult();
    }

    public Long rpush(IRedisKey key, Serializable id, String... val) {
        Response<Long> setResult = execute(new Param(key, id, val), Tool.rpush());
        return setResult.getResult();
    }


    public Long lpush(IRedisKey key, Serializable id, String... val) {
        Response<Long> setResult = execute(new Param(key, id, val), Tool.lpush());
        return setResult.getResult();
    }

    public String rpop(IRedisKey key, Serializable id) {
        Response<String> result = execute(new Param(key, id), Tool.rpop());
        return result.getResult();
    }

    public String lpop(IRedisKey key, Serializable id) {
        Response<String> result = execute(new Param(key, id), Tool.lpop());
        return result.getResult();
    }

    public Long llen(IRedisKey key, Serializable id) {
        Response<Long> result = execute(new Param(key, id), Tool.llen());
        return result.getResult();
    }

    public List<String> lrang(IRedisKey key, Serializable id, long start, long end) {
        Response<List<String>> result = execute(new Param(key, id, start, end), Tool.lrange());
        return result.getResult();
    }

    public String lindex(IRedisKey key, Serializable id, long index) {
        Response<String> result = execute(new Param(key, id, index), Tool.lindex());
        return result.getResult();
    }

    public Long lrem(IRedisKey key, Serializable id, long count, String value) {
        Response<Long> result = execute(new Param(key, id, count, value), Tool.lrem());
        return result.getResult();
    }

    public String lset(IRedisKey key, Serializable id, long index, String value) {
        Response<String> result = execute(new Param(key, id, index, value), Tool.lset());
        return result.getResult();
    }

    public Long sadd(IRedisKey key, Serializable id, String value) {
        Response<Long> result = execute(new Param(key, id, value), Tool.sadd());
        return result.getResult();
    }

    public String spop(IRedisKey key, Serializable id) {
        Response<String> result = execute(new Param(key, id), Tool.spop());
        return result.getResult();
    }

    public Set<String> smembers(IRedisKey key, Serializable id) {
        Response<Set<String>> result = execute(new Param(key, id), Tool.smembers());
        return result.getResult();
    }

    public Boolean sismember(IRedisKey key, Serializable id, String value) {
        Response<Boolean> result = execute(new Param(key, id, value), Tool.sismember());
        return result.getResult();
    }

    public Long scard(IRedisKey key, Serializable id) {
        Response<Long> result = execute(new Param(key, id), Tool.scard());
        return result.getResult();
    }

    public String srandmember(IRedisKey key, Serializable id) {
        Response<String> result = execute(new Param(key, id), Tool.srandmember());
        return result.getResult();
    }

    public Long srem(IRedisKey key, Serializable id, String... members) {
        Response<Long> result = execute(new Param(key, id, members), Tool.srem());
        return result.getResult();
    }

    public List<String> sort(IRedisKey key, Serializable id) {
        Response<List<String>> result = execute(new Param(key, id), Tool.sort());
        return result.getResult();
    }

    public List<String> sort(IRedisKey key, Serializable id, SortingParams sortingParams) {
        Response<List<String>> result = execute(new Param(key, id, sortingParams), Tool.sortByParam());
        return result.getResult();
    }

    public Long zadd(IRedisKey key, Serializable id, double score, String member) {
        Response<Long> result = execute(new Param(key, id, score, member), Tool.zadd());
        return result.getResult();
    }

    public Long zadd(IRedisKey key, Serializable id, Map<String, Double> map) {
        Response<Long> result = execute(new Param(key, id, map), Tool.zaddMap());
        return result.getResult();
    }

    public Long zcard(IRedisKey key, Serializable id) {
        Response<Long> result = execute(new Param(key, id), Tool.zcard());
        return result.getResult();
    }

    public Double zincrby(IRedisKey key, Serializable id, double score, String member) {
        Response<Double> result = execute(new Param(key, id, score, member), Tool.zincrby());
        return result.getResult();
    }

    public Set<String> zrange(IRedisKey key, Serializable id, long start, long end) {
        Response<Set<String>> result = execute(new Param(key, id, start, end), Tool.zrange());
        return result.getResult();
    }

    public Set<Tuple> zrangeWithScores(IRedisKey key, Serializable id, long min, long max) {
        Response<Set<Tuple>> result = execute(new Param(key, id, min, max), Tool.zrangeWithScores());
        return result.getResult();
    }

    public Set<String> zrevrange(IRedisKey key, Serializable id, long start, long end) {
        Response<Set<String>> result = execute(new Param(key, id, start, end), Tool.zrevrange());
        return result.getResult();
    }

    public Set<Tuple> zrevrangeWithScores(IRedisKey key, Serializable id, long min, long max) {
        Response<Set<Tuple>> result = execute(new Param(key, id, min, max), Tool.zrevrangeWithScores());
        return result.getResult();
    }

    public Long zrem(IRedisKey key, Serializable id, String... members) {
        Response<Long> result = execute(new Param(key, id, members), Tool.zrem());
        return result.getResult();
    }

    public Long zremrangebyrank(IRedisKey key, Serializable id, long start, long end) {
        Response<Long> result = execute(new Param(key, id, start, end), Tool.zremrangebyrank());
        return result.getResult();
    }

    public Long zremrangebyscore(IRedisKey key, Serializable id, long start, long end) {
        Response<Long> result = execute(new Param(key, id, start, end), Tool.zremrangebyscore());
        return result.getResult();
    }

    public Long hsetnx(IRedisKey key, Serializable id, String field, String member) {
        Response<Long> result = execute(new Param(key, id, field, member), Tool.hsetnx());
        return result.getResult();
    }

    public Long setnx(IRedisKey key, Serializable id, String value) {
        Response<Long> result = execute(new Param(key, id, value), Tool.setnx());
        return result.getResult();
    }

}
