package com.my.mybatis.executor;

import com.my.mybatis.mapping.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * sql执行器
 */
public interface Executor {

    <T> List<T> query(MappedStatement ms, Object parameter) throws SQLException;

    int update(MappedStatement ms, Object parameter);

}
