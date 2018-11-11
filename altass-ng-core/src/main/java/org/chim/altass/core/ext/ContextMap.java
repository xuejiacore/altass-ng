package org.chim.altass.core.ext;

import org.chim.altass.base.cache.BasicRedisKey;
import org.chim.altass.base.utils.type.StringUtil;
import org.chim.altass.core.executor.ExecuteContext;
import org.chim.altass.toolkit.RedissonToolkit;
import org.redisson.api.RMap;

import java.util.*;

/**
 * Class Name: ContextMap
 * Create Date: 18-1-4 下午1:37
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@SuppressWarnings("unchecked")
public class ContextMap extends HashMap<String, ExecuteContext> {

    private static final long serialVersionUID = 7950307868298731621L;

    private BasicRedisKey key = null;
    private String executeId = null;

    public ContextMap(BasicRedisKey key, String executeId) {
        this.key = key;
        this.executeId = executeId;
    }

    @Override
    public ExecuteContext put(String executorId, ExecuteContext value) {
        RMap<String, String> map = RedissonToolkit.getInstance().getMap(this.key, this.executeId);
        map.put(executorId, this.executeId);
        return value;
    }

    @Override
    public ExecuteContext get(Object key) {
        RMap<String, String> map = RedissonToolkit.getInstance().getMap(this.key, this.executeId);
        String existsMainContext = map.get(key);
        if (!StringUtil.isEmpty(existsMainContext)) {
            return new ExecuteContext((String) key);
        } else {
            return null;
        }
    }

    @Override
    public Collection<ExecuteContext> values() {
        RMap<String, String> map = RedissonToolkit.getInstance().getMap(this.key, this.executeId);
        Set<String> keySet = map.keySet();
        Collection<ExecuteContext> contexts = new ArrayList<ExecuteContext>();
        for (String key : keySet) {
            contexts.add(new ExecuteContext(key));
        }
        return contexts;
    }

    public BasicRedisKey getKey() {
        return key;
    }

    public String getExecuteId() {
        return executeId;
    }

    public Map build() {
        RMap<String, String> map = RedissonToolkit.getInstance().getMap(this.key, this.executeId);
        Map<String, Map> contextMap = new HashMap<>();
        for (String key : map.keySet()) {
            contextMap.put(key, new ExecuteContext(key).buildMapObj());
        }
        return contextMap;
    }


}
