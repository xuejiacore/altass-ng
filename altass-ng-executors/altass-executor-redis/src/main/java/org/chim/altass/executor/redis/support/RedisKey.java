package org.chim.altass.executor.redis.support;

import org.chim.altass.executor.redis.bean.IRedisKey;
import org.chim.altass.executor.redis.constant.KeyType;
import org.chim.altass.executor.redis.constant.OpType;

/**
 * Class Name: RedisKey
 * Create Date: 11/5/18 11:38 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class RedisKey implements IRedisKey {

    private static final int DEFAULT_NONE_EXPIRE = 0;

    private String namespace = "default";

    private KeyType keyType = null;

    private int expire = DEFAULT_NONE_EXPIRE;

    private String keyName = null;

    private String cmd = null;

    public RedisKey(String namespace, KeyType keyType, String cmd, String keyName, int expire) {
        this.namespace = namespace;
        this.keyType = keyType;
        this.cmd = cmd;
        this.expire = expire;
        this.keyName = keyName;
    }

    public RedisKey(String namespace, KeyType keyType, String keyName, int expire) {
        this(namespace, keyType, null, keyName, expire);
    }

    public RedisKey(KeyType keyType, String cmd, String keyName, int expire) {
        this.keyType = keyType;
        this.expire = expire;
        this.cmd = cmd;
        this.keyName = keyName;
    }

    public RedisKey(KeyType keyType, String keyName, int expire) {
        this(keyType, null, keyName, expire);
    }

    public RedisKey(String cmd, String keyName) {
        Cmd c = Cmd.valueOf(cmd.toUpperCase());
        this.keyName = keyName;
        this.cmd = cmd;
        this.keyType = c.getKeyType();
    }

    public RedisKey(KeyType keyType, String keyName) {
        this.keyType = keyType;
        this.keyName = keyName;
    }

    public RedisKey(String cmd, String keyName, int expire) {
        this.cmd = cmd;
        this.keyName = keyName;
        this.expire = expire;
    }

    public RedisKey(String namespace, String cmd, String keyName, int expire) {
        this(cmd, keyName);
        this.namespace = namespace;
        this.expire = expire;
    }

    @Override
    public String namespace() {
        return this.namespace;
    }

    @Override
    public KeyType keyType() {
        return this.keyType;
    }

    @Override
    public int expire() {
        return this.expire;
    }

    @Override
    public String keyName() {
        return this.keyName;
    }

    @Override
    public String serverGroup() {
        return null;
    }

    @Override
    public String cmd() {
        return this.cmd;
    }

    @Override
    public OpType op() {
        return null;
    }
}
