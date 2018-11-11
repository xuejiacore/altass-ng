package org.chim.altass.executor.redis.bean;

import org.chim.altass.executor.redis.constant.KeyType;
import org.chim.altass.executor.redis.constant.OpType;
import org.redisson.api.RExpirable;

import java.io.Serializable;

/**
 * Class Name: Param
 * Create Date: 11/5/18 11:07 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class Param {

    private IRedisKey redisKey = null;
    private OpType op = null;
    private KeyType keyType = null;
    private Serializable keyId = null;
    private Object[] values = null;
    private RExpirable rExpirable = null;

    public Param() {

    }

    public Param(IRedisKey key) {

    }

    public Param(IRedisKey key, Serializable id, Object... values) {
        this.redisKey = key;
        this.keyType = key.keyType();
        this.values = values;
        this.keyId = id;
    }

    public IRedisKey getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(IRedisKey redisKey) {
        this.redisKey = redisKey;
    }

    public OpType getOp() {
        return op;
    }

    public void setOp(OpType op) {
        this.op = op;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public Serializable getKeyId() {
        return keyId;
    }

    public void setKeyId(Serializable keyId) {
        this.keyId = keyId;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public String buildKey(Serializable keyId) {
        String namespace = redisKey.namespace();
        if (namespace == null || namespace.length() == 0) {
            throw new IllegalArgumentException("The return value of RedisKey#namespace() COULD NOT BE EMPTY.");
        }
        return namespace + IRedisKey.KEY_SEPARATOR +
                redisKey.keyName() + IRedisKey.KEY_ID_SEPARATOR +
                keyId;
    }

    public String buildKey() {
        return buildKey(getKeyId());
    }

    public RExpirable getrExpirable() {
        return rExpirable;
    }

    public void setrExpirable(RExpirable rExpirable) {
        this.rExpirable = rExpirable;
    }
}
