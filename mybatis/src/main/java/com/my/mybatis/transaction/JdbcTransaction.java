package com.my.mybatis.transaction;

import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * jdbc事务实现
 */
public class JdbcTransaction implements Transaction {

    private DataSource dataSource;

    private Connection connection;

    private boolean autoCommit;

    public JdbcTransaction(DataSource dataSource, boolean autoCommit) {
        this.dataSource = dataSource;
        this.autoCommit = autoCommit;
    }

    @SneakyThrows
    @Override
    public Connection getConnection() {
        connection = dataSource.getConnection();
        connection.setAutoCommit(autoCommit);
        return connection;
    }

    @SneakyThrows
    @Override
    public void commit() {
        if (autoCommit) {
            return;
        }
        connection.commit();
    }

    @SneakyThrows
    @Override
    public void rollback() {
        if (autoCommit) {
            return;
        }
        connection.rollback();
    }

    @SneakyThrows
    @Override
    public void close() {
        connection.close();
    }
}
