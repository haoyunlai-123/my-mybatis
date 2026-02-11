package com.my.mybatis;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestBatch {

    @Test
    public void test() throws ClassNotFoundException, SQLException {
        // 加载数据库驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 建立数据库连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://root:1234@localhost:3306/my-mybatis");

        PreparedStatement statement = connection.prepareStatement("insert into `t_user` (`name`, `age`) values (?, ?)");
        for (int i = 0; i < 1000; i++) {
            statement.setString(1, "name" + i);
            statement.setInt(2, i);
            statement.addBatch();
            if ((i + 1) % 100 == 0) {
                statement.executeBatch();
                statement.clearBatch();
            }
        }

        // 释放资源
        connection.close();
        statement.close();

    }

}
