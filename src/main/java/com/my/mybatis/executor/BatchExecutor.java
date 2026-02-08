package com.my.mybatis.executor;

import com.my.mybatis.mapping.MappedStatement;

import java.util.Collections;
import java.util.List;

/**
 * 批处理执行器
 */
public class BatchExecutor implements Executor {
    @Override
    public <T> List<T> query(MappedStatement ms, Object parameter) {
        return Collections.emptyList();
    }

    @Override
    public int update(MappedStatement ms, Object parameter) {
        return 0;
    }
}
