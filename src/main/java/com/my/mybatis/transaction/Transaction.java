package com.my.mybatis.transaction;

import java.sql.Connection;

/**
 * 事物管理
 */
public interface Transaction {

    Connection getConnection();

    void commit();

    void rollback();

    void close();

}
