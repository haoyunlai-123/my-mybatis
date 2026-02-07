package com.my.mybatis.binding;

import com.my.mybatis.annotation.Select;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.*;

public class MapperProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Connection connection = getConnection();

        Select select = method.getAnnotation(Select.class);
        if (select == null) {
            throw new RuntimeException("方法无注解");
        }
        String sql = select.value();

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.execute();

        ResultSet resultSet = ps.getResultSet();
        while (resultSet.next()){
            System.out.println(resultSet.getString("name") + "---" +  resultSet.getInt("age"));
        }

        connection.close();
        resultSet.close();
        ps.close();

        return null;
    }

    @SneakyThrows
    private static Connection getConnection() {
        // 加载数据库驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 建立数据库连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://root:1234@localhost:3306/my-mybatis");
        return connection;
    }
}
