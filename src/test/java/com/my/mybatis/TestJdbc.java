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

        // 建立数据库连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://root:1234@localhost:3306/my-mybatis");

        PreparedStatement ps = connection.prepareStatement("select * from t_user");
        ps.execute();

        ResultSet resultSet = ps.getResultSet();
        while (resultSet.next()){
            System.out.println(resultSet.getString("name") + "---" +  resultSet.getInt("age"));
        }

        connection.close();
        resultSet.close();
        ps.close();
    }

}
