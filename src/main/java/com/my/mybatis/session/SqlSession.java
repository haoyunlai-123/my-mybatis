package com.my.mybatis.session;

import com.my.mybatis.mapping.MappedStatement;

import java.util.List;

/**
 * 操作增删改查
 */
public interface SqlSession {

    <T> T selectOne(String statementId, Object parameter);

    <T> List<T> selectList(String statementId, Object parameter);

    int insert(String statement, Object parameter);

    int update(String statement, Object parameter);

    int delete(String statement, Object parameter);

    <T> T getMapper(Class<T> mapper);

    Configuration getConfiguration();
}
