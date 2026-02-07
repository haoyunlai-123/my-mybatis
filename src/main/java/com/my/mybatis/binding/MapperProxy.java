package com.my.mybatis.binding;

import cn.hutool.aop.proxy.ProxyFactory;
import cn.hutool.core.util.ReflectUtil;
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

import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
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

        // 拿到mapper的返回值类型
        // 这段代码用于获取方法的返回类型：
        // - 如果返回类型是带泛型的（如 List<User>)，就取出其第一个泛型参数作为实际返回类型。
        // - 如果返回类型不是泛型（如 User），就直接使用该返回类型
        Class returnType = null;
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            returnType =  (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
        } else if (genericReturnType instanceof Class) {
            returnType = (Class) genericReturnType;
        }

        ResultSet resultSet = ps.getResultSet();

        // 拿到sql返回字段名称
        List<String> columnList = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            columnList.add(metaData.getColumnName(i + 1));
        }

        List list = new ArrayList();
        while (resultSet.next()){
            System.out.println(resultSet.getString("name") + "---" +  resultSet.getInt("age"));
            Object instance = returnType.newInstance();
            // 反射为字段赋值
            ReflectUtil.setFieldValue(instance, "", null);
        }

        connection.close();
        resultSet.close();
        ps.close();

        return list;
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
