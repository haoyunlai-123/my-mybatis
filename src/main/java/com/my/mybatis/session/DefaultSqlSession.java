package com.my.mybatis.session;

import com.my.mybatis.binding.MapperProxyFactory;
import com.my.mybatis.executor.Executor;
import com.my.mybatis.mapping.MappedStatement;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor;

    @Override
    public <T> T selectOne(String statementId, Object parameter) {
        List<T> list = selectList(statementId, parameter);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new RuntimeException("期望通过selectOne返回一条结果，但是查到了多条数据" + list.size());
        } else {
            return null;
        }
    }

    @Override
    public <T> List<T> selectList(String statementId, Object parameter) {
        MappedStatement ms = configuration.getMappedStatement(statementId);
        return executor.query(ms, parameter);
    }

    @Override
    public int insert(String statementId, Object parameter) {
        MappedStatement ms = configuration.getMappedStatement(statementId);
        return executor.update(ms, parameter);
    }

    @Override
    public int update(String statementId, Object parameter) {
        MappedStatement ms = configuration.getMappedStatement(statementId);
        return executor.update(ms, parameter);
    }

    @Override
    public int delete(String statementId, Object parameter) {
        MappedStatement ms = configuration.getMappedStatement(statementId);
        return executor.update(ms, parameter);
    }

    @Override
    public <T> T getMapper(Class<T> mapper) {
        return MapperProxyFactory.getProxy(mapper, this);
    }

}
