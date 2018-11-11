package org.chim.altass.core.ext;


import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.EXmlParser;
import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.toolkit.RedissonToolkit;
import org.redisson.api.RLock;
import org.redisson.api.RMap;

import java.util.HashMap;

/**
 * Class Name: ExecEntryMap
 * Create Date: 18-1-4 上午10:35
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@SuppressWarnings("unchecked")
public class ExecEntryMap extends HashMap<String, Object> {

    private static final long serialVersionUID = 3748619095704178164L;

    public ExecEntryMap() {
    }

    @Override
    public Object get(Object key) {
        RLock lock = RedissonToolkit.getInstance().getLock(EurekaSystemRedisKey.EUREKA$GLOBAL_EXES_LOCK, String.valueOf(key));

        RMap<String, String> map = RedissonToolkit.getInstance().getMap(EurekaSystemRedisKey.EUREKA$GLOBAL_EXES, 0);
        RMap<String, String> subMap = RedissonToolkit.getInstance().getMap(EurekaSystemRedisKey.EUREKA$GLOBAL_EXES_SUB, 0);

        String clzName = subMap.get(key);
        if (clzName == null) {
            return null;
        }

        Class entryClz;
        Object obj = null;
        try {
            entryClz = Class.forName(clzName);

            String entryXml = map.get(key);

            obj = EXmlParser.fromXml(entryXml, entryClz);
        } catch (XmlParserException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (lock.isLocked())
                lock.unlock();
        }
        return obj;
    }

    @Override
    public Object put(String key, Object value) {
        RLock lock = RedissonToolkit.getInstance().getLock(EurekaSystemRedisKey.EUREKA$GLOBAL_EXES_LOCK, key);

        RMap<String, String> map = RedissonToolkit.getInstance().getMap(EurekaSystemRedisKey.EUREKA$GLOBAL_EXES, 0);
        RMap<String, String> subMap = RedissonToolkit.getInstance().getMap(EurekaSystemRedisKey.EUREKA$GLOBAL_EXES_SUB, 0);

        try {
            subMap.put(key, value.getClass().getName());
            map.put(key, EXmlParser.toXml(value));
        } catch (XmlParserException e) {
            e.printStackTrace();
        }

        if (lock.isLocked())
            lock.unlock();
        return super.put(key, value);
    }

    @Override
    public boolean containsKey(Object key) {
        RMap<String, String> map = RedissonToolkit.getInstance().getMap(EurekaSystemRedisKey.EUREKA$GLOBAL_EXES, 0);
        return map.containsKey(key);
    }

    @Override
    public Object remove(Object key) {
        RLock lock = RedissonToolkit.getInstance().getLock(EurekaSystemRedisKey.EUREKA$GLOBAL_EXES_LOCK, (String) key);

        RMap<String, String> map = RedissonToolkit.getInstance().getMap(EurekaSystemRedisKey.EUREKA$GLOBAL_EXES, 0);
        RMap<String, String> subMap = RedissonToolkit.getInstance().getMap(EurekaSystemRedisKey.EUREKA$GLOBAL_EXES_SUB, 0);
        String removeClz = subMap.remove(key);

        if (removeClz == null) {
            return null;

        }
        Class entryClz;
        Object obj = null;
        try {
            entryClz = Class.forName(removeClz);
            String removeEntryXml = map.remove(key);
            obj = EXmlParser.fromXml(removeEntryXml, entryClz);
        } catch (XmlParserException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        if (lock.isLocked())
            lock.unlock();

        return obj;
    }
}
