package com.my.mybatis.builder;

import cn.hutool.core.util.ClassUtil;
import com.my.mybatis.annotation.Select;
import com.my.mybatis.mapping.MappedStatement;
import com.my.mybatis.session.Configuration;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * XML配置构建器
 */
public class XMLConfigBuilder {

    public Configuration parse() {
        // 解析mapper
        Configuration configuration = new Configuration();
        parseMapper(configuration);
        return configuration;
    }

    private void parseMapper(Configuration configuration) {

        Set<Class<?>> classes = ClassUtil.scanPackage("com.my.demo.mapper");
        for (Class<?> aClass : classes) {
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                Select select = method.getAnnotation(Select.class);
                if (select == null) {
                    throw new RuntimeException("方法无注解");
                }
                String originalSql = select.value();

                Class returnType = null;
                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType) {
                    returnType =  (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
                } else if (genericReturnType instanceof Class) {
                    returnType = (Class) genericReturnType;
                }

                MappedStatement mappedStatement = MappedStatement.builder()
                        .id(aClass.getName() + "." + method.getName())
                        .sql(originalSql)
                        .returnType(returnType)
                        .build();
                configuration.addMappedStatement(mappedStatement);
            }
        }
    }

    public static void main(String[] args) {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parse();
    }
}
