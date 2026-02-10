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

        Long sqlStart = System.currentTimeMillis();

        PreparedStatement ps = connection.prepareStatement("select * from t_user where id = ? and name = ?");
        ps.setInt(1,1);
        ps.setString(2, "xyg");
        ps.execute();

        System.out.println("执行SQL耗时: " + (System.currentTimeMillis() - sqlStart) + "ms");

        ResultSet resultSet = ps.getResultSet();
        while (resultSet.next()){
            System.out.println(resultSet.getString("name") + "---" +  resultSet.getInt("age"));
        }

        connection.close();
        resultSet.close();
        ps.close();
    }

    @Test
    public void test()  throws Exception {

    }

}
