package org.chim.altass.core.ext;

import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.toolkit.RedissonToolkit;
import org.redisson.api.RMap;

import java.util.HashMap;

/**
 * Class Name: ContextCacheMap
 * Create Date: 18-1-5 下午6:48
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@SuppressWarnings("unchecked")
public class ContextCacheMap extends HashMap<String, ContextMap> {
    private static final long serialVersionUID = -6720340802771756496L;

    @Override
    public ContextMap get(Object key) {
        RMap<String, String> map = RedissonToolkit.getInstance().getMap(EurekaSystemRedisKey.EUREKA$GLOBAL_CTX_CACHE, 0);
        String contextId = map.get(key);

        return contextId == null ? null : new ContextMap(EurekaSystemRedisKey.EUREKA$SYSTEM_FLUX_CONTEXT, contextId);
    }

    @Override
    public ContextMap put(String key, ContextMap value) {
        RMap<String, String> map = RedissonToolkit.getInstance().getMap(EurekaSystemRedisKey.EUREKA$GLOBAL_CTX_CACHE, 0);
        map.put(key, value.getExecuteId());
        return new ContextMap(EurekaSystemRedisKey.EUREKA$SYSTEM_FLUX_CONTEXT, key);
    }
}
