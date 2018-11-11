package org.chim.altass.core.ext;

import org.chim.altass.base.cache.BasicRedisKey;
import org.chim.altass.toolkit.RedissonToolkit;
import org.redisson.api.RMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class Name: RMap
 * Create Date: 18-1-4 上午9:43
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@SuppressWarnings("unchecked")
public class DMap<K, V> extends HashMap<K, V> {
    private static final long serialVersionUID = -8938562781987987547L;

    private String executeId = null;
    private BasicRedisKey key = null;

    public DMap(BasicRedisKey key, String executeId) {
        this.executeId = executeId;
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    public DMap(BasicRedisKey key, String executeId, Map data) {
        this.executeId = executeId;
        this.key = key;
        if (data == null)
            return;

        RMap<K, V> map = RedissonToolkit.getInstance().getMap(this.key, this.executeId);
        map.putAll(data);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V put(K key, V value) {
        RMap<K, V> map = RedissonToolkit.getInstance().getMap(this.key, this.executeId);
        return map.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        RMap<K, V> map = RedissonToolkit.getInstance().getMap(this.key, this.executeId);
        return map.get(key);
    }

    @Override
    public Set<K> keySet() {
        RMap map = RedissonToolkit.getInstance().getMap(this.key, this.executeId);
        return map.keySet();
    }

    public Map build() {
        RMap map = RedissonToolkit.getInstance().getMap(this.key, this.executeId);
        Map d = new HashMap();
        for (Object key : map.keySet()) {
            d.put(key, map.get(key));
        }
        return d;
    }
}
