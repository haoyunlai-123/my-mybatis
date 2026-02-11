package com.my.mybatis.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一级缓存，永久缓存
 */
public class PerpetualCache implements Cache {

    private String id;

    private Map<Object, Object> cacheMap = new ConcurrentHashMap<>();

    public PerpetualCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        cacheMap.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return cacheMap.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return cacheMap.remove(key);
    }

    @Override
    public void clear() {
        cacheMap.clear();
    }
}
