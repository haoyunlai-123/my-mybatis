package com.my.mybatis;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestJdbc {

    @Test
    public void testJdbc() throws  Exception{
        // 加载数据库驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        Long start = System.currentTimeMillis();
        // 建立数据库连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://root:1234@localhost:3306/my-mybatis");
        System.out.println("连接数据库耗时: " + (System.currentTimeMillis() - start) + "ms");
        System.out.println("是否自动提交: " + connection.getAutoCommit());

        connection.setAutoCommit(false);
        // 设置事务隔离级别
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        Long sqlStart = System.currentTimeMillis();
        PreparedStatement ps = connection.prepareStatement("insert into `t_user` (id, name) values (10, `铁手`)");
        ps.execute();
        System.out.println("执行SQL耗时: " + (System.currentTimeMillis() - sqlStart) + "ms");
        System.out.println(ps.getUpdateCount());

        try {
            connection.commit();
            int i = 1 / 0;
        } catch (Exception e) {
            connection.rollback();
            throw new RuntimeException(e);
        }

        connection.close();
        ps.close();
    }

    @Test
    public void test()  throws Exception {

    }

}
