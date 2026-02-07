package com.my.mybatis.binding;

import cn.hutool.aop.proxy.ProxyFactory;
import com.my.demo.entity.User;
import com.my.demo.mapper.UserMapper;
import com.my.mybatis.annotation.Param;
import com.my.mybatis.annotation.Select;
import com.my.mybatis.parsing.GenericTokenParser;
import com.my.mybatis.parsing.ParameterMappingTokenHandler;
import com.my.mybatis.type.IntegerTypeHandler;
import com.my.mybatis.type.StringTypeHandler;
import com.my.mybatis.type.TypeHandler;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapperProxy implements InvocationHandler {

    private Map<Class, TypeHandler> typeHandlerMap = new HashMap<>();

    {
        typeHandlerMap.put(Integer.class, new IntegerTypeHandler());
        typeHandlerMap.put(String.class, new StringTypeHandler());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Connection connection = getConnection();

        Select select = method.getAnnotation(Select.class);
        if (select == null) {
            throw new RuntimeException("方法无注解");
        }
        String originalSql = select.value();

        // sql解析： #{}
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler();
        GenericTokenParser tokenParser = new GenericTokenParser("#{", "}", handler);
        String sql = tokenParser.parse(originalSql);
        List<String> parameterMappings = handler.getParameterMappings();

        PreparedStatement ps = connection.prepareStatement(sql);
        /*ps.setInt(1, (Integer) args[0]);
        ps.setString(2, String.valueOf(args[1]));*/

        // 建立mapper层方法参数名称和值的映射
        Map<String,Object> paramValueMap = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Param param = parameter.getAnnotation(Param.class);
            if (param == null) { continue; }
            String paramName = param.value();
            paramValueMap.put(paramName, args[i]);
        }

        // 根据映射给sql参数赋值
        for (int i = 0; i < parameterMappings.size(); i++) {
            String jdbcColumnName = parameterMappings.get(i);
            Object val = paramValueMap.get(jdbcColumnName);
            TypeHandler typeHandler = typeHandlerMap.get(val.getClass());
            if (typeHandler == null) {
                ps.setObject(i + 1, val);
            } else {
                typeHandler.setParameter(ps, i + 1, val);
            }

        }

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

    public static void main(String[] args) {
        UserMapper userMapper = MapperProxyFactory.getProxy(UserMapper.class);
        List<User> zq = userMapper.selectList(1, "zq");
        System.out.println(zq);
    }
}
