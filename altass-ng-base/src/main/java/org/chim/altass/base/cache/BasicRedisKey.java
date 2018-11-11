package org.chim.altass.base.cache;

/**
 * Class Name: BasicRedisKey
 * Create Date: 17-12-26 下午5:29
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface BasicRedisKey {

    String KEY_ID_SEPARATOR = "#";

    /**
     * 构建redis key
     *
     * @return 构建枚举对应的redis key
     */
    String redisKey();

    /**
     * 获得键的描述
     *
     * @return 获得键的描述
     */
    String desc();

    Long ttl();
}
