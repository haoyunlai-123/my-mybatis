package com.my.mybatis.executor.statement;

import java.sql.Connection;
import java.sql.Statement;

/**
 * 语句处理器
 */
public interface StatementHandler {

    Statement prepare(Connection connection);

    void parameterize(Statement statement);

    <T> T query(Statement statement);

     int update(Statement statement);
}
