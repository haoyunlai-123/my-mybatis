package com.my.mybatis.executor.resultset;

import com.my.mybatis.mapping.MappedStatement;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * 结果处理器
 */
public interface ResultSetHandler {

    <T> List<T> handleResultSets(MappedStatement ms, PreparedStatement ps);

}
