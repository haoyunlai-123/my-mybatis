package com.my.mybatis.executor;

import cn.hutool.core.util.ReflectUtil;
import com.my.mybatis.annotation.Param;
import com.my.mybatis.annotation.Select;
import com.my.mybatis.mapping.BoundSql;
import com.my.mybatis.mapping.MappedStatement;
import com.my.mybatis.parsing.GenericTokenParser;
import com.my.mybatis.parsing.ParameterMappingTokenHandler;
import com.my.mybatis.session.Configuration;
import com.my.mybatis.type.TypeHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Parameter;
import java.sql.*;
import java.util.*;

/**
 * 简单执行器
 */
@Slf4j
public class SimpleExecutor implements Executor {

    private Configuration configuration;

    public SimpleExecutor(Configuration configuration) {
        this.configuration = configuration;
    }

    @SneakyThrows
    @Override
    public <T> List<T> query(MappedStatement ms, Object parameter) {

        Connection connection = getConnection();

        PreparedStatement ps = execute(ms, parameter, connection);

        // 拿到mapper的返回值类型
        // 这段代码用于获取方法的返回类型：
        // - 如果返回类型是带泛型的（如 List<User>)，就取出其第一个泛型参数作为实际返回类型。
        // - 如果返回类型不是泛型（如 User），就直接使用该返回类型
        /*Class returnType = null;
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            returnType =  (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
        } else if (genericReturnType instanceof Class) {
            returnType = (Class) genericReturnType;
        }*/

        List<T> list = configuration.newResultSetHandler().handleResultSets(ms, ps);

        connection.close();

        return list;
    }

    @SneakyThrows
    @Override
    public int update(MappedStatement ms, Object parameter) {

        Connection connection = getConnection();

        PreparedStatement ps = execute(ms, parameter, connection);

        // 拿到操作数
        int updateCount = ps.getUpdateCount();

        connection.close();
        ps.close();

        return updateCount;
    }

    @SneakyThrows
    @Override
    public int insert(MappedStatement ms, Object parameter) {
        Connection connection = getConnection();
        // insert into t_user(name, age) values(#{name}, #{age})

        PreparedStatement ps = execute(ms, parameter, connection);

        // 拿到操作数
        int insertCount = ps.getUpdateCount();

        connection.close();
        ps.close();

        return insertCount;
    }

    @SneakyThrows
    @Override
    public int delete(MappedStatement ms, Object parameter) {
        Connection connection = getConnection();
        // delete from t_user where id = #{id}
        PreparedStatement ps = execute(ms, parameter, connection);

        // 拿到操作数
        int deleteCount = ps.getUpdateCount();

        connection.close();
        ps.close();

        return deleteCount;
    }


    @SneakyThrows
    private void setParam(PreparedStatement ps, List<String> parameterMappings, Object parameter) {
        Map<Class, TypeHandler> typeHandlerMap = configuration.getTypeHandlerMap();
        // 根据映射给sql参数赋值
        Map<String, Object> paramValueMap = (Map<String, Object>) parameter;
        // paramValueMap: "user" ==> user对象
        for (int i = 0; i < parameterMappings.size(); i++) {
            String jdbcColumnName = parameterMappings.get(i);
            Object val = paramValueMap.get(jdbcColumnName);

            // user.name ==> name
            // 反射获取user对象的属性值
            if (jdbcColumnName.contains(".")) {
                String[] split = jdbcColumnName.split("\\.");
                String objectName = split[0];
                String fieldName = split[1];
                Object objectVal = paramValueMap.get(objectName);
                val = ReflectUtil.getFieldValue(objectVal, fieldName);
            }

            TypeHandler typeHandler = typeHandlerMap.get(val.getClass());
            if (typeHandler == null) {
                ps.setObject(i + 1, val);
            } else {
                typeHandler.setParameter(ps, i + 1, val);
            }

        }
    }

    @SneakyThrows
    private PreparedStatement execute(MappedStatement ms, Object parameter, Connection connection) {

        BoundSql boundSql = ms.getBoundSql();

        PreparedStatement ps = connection.prepareStatement(boundSql.getSql());

        // 给sql参数赋值
        setParam(ps, boundSql.getParameterMappings(), parameter);

        log.info("执行SQL: " + boundSql.getSql());
        log.info("参数: " + parameter);
        ps.execute();

        return ps;
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
