package com.my.mybatis.executor;

import com.my.mybatis.cache.Cache;
import com.my.mybatis.mapping.MappedStatement;

import java.util.Collections;
import java.util.List;

/**
 * 缓存执行器
 */
public class CacheExecutor implements Executor {

    // 被装饰者
    private Executor delegate;

    public CacheExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> List<T> query(MappedStatement ms, Object parameter) {
        Cache cache = ms.getCache();
        String cacheKey = ms.getCacheKey(parameter);
        Object cacheData = cache.getObject(cacheKey);
        if (cacheData != null) {
            return (List<T>) cacheData;
        }
        List<T> list = delegate.query(ms, parameter);
        cache.putObject(cacheKey, list);
        return list;
    }

    @Override
    public int update(MappedStatement ms, Object parameter) {
        ms.getCache().clear();
        return delegate.update(ms, parameter);
    }

    @Override
    public int insert(MappedStatement ms, Object parameter) {
        ms.getCache().clear();
        return delegate.insert(ms, parameter);
    }

    @Override
    public int delete(MappedStatement ms, Object parameter) {
        ms.getCache().clear();
        return delegate.delete(ms, parameter);
    }

    @Override
    public void commit() {
        delegate.commit();
    }

    @Override
    public void rollback() {
        delegate.rollback();
    }

    @Override
    public void close() {
        delegate.close();
    }
}
