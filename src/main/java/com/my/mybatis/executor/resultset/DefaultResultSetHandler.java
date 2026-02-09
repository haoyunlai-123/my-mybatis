package com.my.mybatis.executor.resultset;

import cn.hutool.core.util.ReflectUtil;
import com.my.mybatis.mapping.MappedStatement;
import com.my.mybatis.session.Configuration;
import com.my.mybatis.type.TypeHandler;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 默认的结果集处理器
 */
public class DefaultResultSetHandler implements ResultSetHandler {

    private final Configuration configuration;

    public DefaultResultSetHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @SneakyThrows
    @Override
    public <T> List<T> handleResultSets(MappedStatement ms, PreparedStatement ps) {
        Class returnType = ms.getReturnType();

        ResultSet resultSet = ps.getResultSet();

        // 拿到sql返回字段名称
        List<String> columnList = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            columnList.add(metaData.getColumnName(i + 1));
        }

        Map<Class, TypeHandler> typeHandlerMap = configuration.getTypeHandlerMap();
        // 将从数据库中获取的一条条数据赋值给实体对象
        List list = new ArrayList();
        while (resultSet.next()){
            System.out.println(resultSet.getString("name") + "---" +  resultSet.getInt("age"));
            Object instance = returnType.newInstance();

            for (String columnName : columnList) {
                Class<?> type = ReflectUtil.getField(returnType, columnName).getType();

                Object value= typeHandlerMap.get(type).getResult(resultSet, columnName);
                // 反射为字段赋值
                ReflectUtil.setFieldValue(instance, columnName, value);
            }

            list.add(instance);
        }
        resultSet.close();
        ps.close();
        return list;
    }
}
