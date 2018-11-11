package org.chim.altass.executor.redis.support;

import org.chim.altass.executor.redis.constant.KeyType;

/**
 * Class Name: Cmd
 * Create Date: 11/5/18 11:52 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
enum Cmd {

    SET(Tool.set(), KeyType.STRING, "set"),
    GET(Tool.get(), KeyType.STRING, "get"),

    INCR(Tool.incr(), KeyType.STRING, "incr"),
    DECR(Tool.decr(), KeyType.STRING, "decr"),

    INCRBY(Tool.incrBy(), KeyType.STRING, "incrBy"),
    DECRBY(Tool.decrBy(), KeyType.STRING, "decrBy"),

    SETNX(Tool.setnx(), KeyType.STRING, "setnx"),
    EXISTS(Tool.exists(), KeyType.COMMON, "exists"),
    TTL(Tool.ttl(), KeyType.COMMON, "ttl"),
    DEL(Tool.del(), KeyType.COMMON, "del"),
    EXPIRE(Tool.expire(), KeyType.COMMON, "expire"),

    HSET(Tool.hset(), KeyType.HASH, "hset"),
    HGET(Tool.hget(), KeyType.HASH, "hget"),
    HMSET(Tool.hmset(), KeyType.HASH, "hmset"),
    HMGET(Tool.hmget(), KeyType.HASH, "hmget"),
    HGETALL(Tool.hgetAll(), KeyType.HASH, "hgetAll"),
    HEXISTS(Tool.hexists(), KeyType.HASH, "hexists"),
    HLEN(Tool.hlen(), KeyType.HASH, "hlen"),
    HINCRBY(Tool.hincrby(), KeyType.HASH, "hincrby"),
    HDEL(Tool.hdel(), KeyType.HASH, "hdel"),
    HSETNX(Tool.hsetnx(), KeyType.HASH, "hsetnx"),

    RPUSH(Tool.rpush(), KeyType.LIST, "rpush"),
    LPUSH(Tool.lpush(), KeyType.LIST, "lpush"),
    LINDEX(Tool.lindex(), KeyType.LIST, "lindex"),
    RPOP(Tool.rpop(), KeyType.LIST, "rpop"),
    LPOP(Tool.lpop(), KeyType.LIST, "lpop"),
    LLEN(Tool.llen(), KeyType.LIST, "llen"),
    LRANGE(Tool.lrange(), KeyType.LIST, "lrange"),
    LREM(Tool.lrem(), KeyType.LIST, "lrem"),
    LSET(Tool.lset(), KeyType.LIST, "lset"),

    SADD(Tool.sadd(), KeyType.SET, "sadd"),
    SPOP(Tool.spop(), KeyType.SET, "spop"),
    SMEMBERS(Tool.smembers(), KeyType.SET, "smembers"),
    SISMEMBER(Tool.sismember(), KeyType.SET, "sismember"),
    SCARD(Tool.scard(), KeyType.SET, "scard"),
    SRANDMEMBER(Tool.srandmember(), KeyType.SET, "srandmember"),
    SREM(Tool.srem(), KeyType.SET, "srem"),
    SORT(Tool.sort(), KeyType.SORTED_SET, "sort"),
    SORTBYPARAM(Tool.sortByParam(), KeyType.SORTED_SET, "sortByParam"),

    ZADD(Tool.zadd(), KeyType.SET, "zadd"),
    ZADDMAP(Tool.zaddMap(), KeyType.SET, "zaddMap"),
    ZCARD(Tool.zcard(), KeyType.SET, "zcard"),
    ZINCRBY(Tool.zincrby(), KeyType.SET, "zincrby"),
    ZRANGE(Tool.zrange(), KeyType.SET, "zrange"),
    ZRANGEWITHSCORES(Tool.zrangeWithScores(), KeyType.SET, "zrangeWithScores"),
    ZREVRANGE(Tool.zrevrange(), KeyType.SET, "zrevrange"),
    ZREVRANGEWITHSCORES(Tool.zrevrangeWithScores(), KeyType.SET, "zrevrangeWithScores"),
    ZREM(Tool.zrem(), KeyType.SET, "zrem"),
    ZREMRANGEBYRANK(Tool.zremrangebyrank(), KeyType.SET, "zremrangebyrank"),
    ZREMRANGEBYSCORE(Tool.zremrangebyscore(), KeyType.SET, "zremrangebyscore"),;

    private Operator operator = null;
    private KeyType keyType = null;
    private String desc = null;

    Cmd(Operator operator, KeyType keyType, String desc) {
        this.operator = operator;
        this.desc = desc;
        this.keyType = keyType;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }
}
