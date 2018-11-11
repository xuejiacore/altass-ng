package org.chim.altass.executor.redis.bean;

import org.chim.altass.executor.redis.constant.KeyType;
import org.chim.altass.executor.redis.constant.OpType;

/**
 * Class Name: RedisKey
 * Create Date: 11/5/18 11:09 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface IRedisKey {
    String KEY_SEPARATOR = ".";
    String KEY_ID_SEPARATOR = "#";
    String KEY_ID_SEPARATOR_COMPATIBLE = "`@~";

    /**
     * redis key 的命名空间，最终为作为redis key 的
     * <p>
     * namespace.keyName#id 作为组合redis key
     *
     * @return key的命名空间
     */
    String namespace();

    /**
     * 获得key的类型，决定了在下层操作是否会进行设置超时时间等操作
     *
     * @return redis key类型
     */
    KeyType keyType();

    /**
     * 获得redis key 的超时设置
     * <p>
     * > 0 设置超时，-1在更新时不设置超时
     *
     * @return 超时设置
     */
    int expire();

    /**
     * 在namespace下，key的名称
     * namespace.keyName#id 组合键
     *
     * @return key name
     */
    String keyName();

    /**
     * 设置redis 操作的配置组，允许在一个项目中使用多个redis实例
     *
     * @return 服务组的名称
     */
    String serverGroup();

    String cmd();

    OpType op();
}
