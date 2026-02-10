package com.my.mybatis.executor;

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
        return delegate.query(ms, parameter);
    }

    @Override
    public int update(MappedStatement ms, Object parameter) {
        return delegate.update(ms, parameter);
    }

    @Override
    public int insert(MappedStatement ms, Object parameter) {
        return delegate.insert(ms, parameter);
    }

    @Override
    public int delete(MappedStatement ms, Object parameter) {
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
