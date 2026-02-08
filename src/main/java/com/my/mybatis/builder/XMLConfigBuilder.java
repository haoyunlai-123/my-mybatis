package com.my.mybatis.builder;

import cn.hutool.core.util.ClassUtil;
import com.my.mybatis.annotation.Delete;
import com.my.mybatis.annotation.Insert;
import com.my.mybatis.annotation.Select;
import com.my.mybatis.annotation.Update;
import com.my.mybatis.mapping.MappedStatement;
import com.my.mybatis.mapping.SqlCommandType;
import com.my.mybatis.session.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * XML配置构建器
 */
public class XMLConfigBuilder {

    private Set<Class> sqlAnnotationSet = new HashSet<>();

    {
        sqlAnnotationSet.add(Select.class);
        sqlAnnotationSet.add(Update.class);
        sqlAnnotationSet.add(Delete.class);
        sqlAnnotationSet.add(Insert.class);
    }

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
                String originalSql = null;
                SqlCommandType sqlCommandType = null;
                for (Class<? extends Annotation> annotationClass : sqlAnnotationSet) {
                    if (method.isAnnotationPresent(annotationClass)) {
                        if (annotationClass == Select.class) {
                            originalSql = method.getAnnotation(Select.class).value();
                            sqlCommandType = SqlCommandType.SELECT;
                        } else if (annotationClass == Update.class) {
                            originalSql = method.getAnnotation(Update.class).value();
                            sqlCommandType = SqlCommandType.UPDATE;
                        } else if (annotationClass == Delete.class) {
                            originalSql = method.getAnnotation(Delete.class).value();
                            sqlCommandType = SqlCommandType.DELETE;
                        } else if (annotationClass == Insert.class) {
                            originalSql = method.getAnnotation(Insert.class).value();
                            sqlCommandType = SqlCommandType.INSERT;
                        }
                        break;
                    }
                }

                Class returnType = null;
                boolean isSelectMany = false;
                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType) {
                    returnType =  (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
                    isSelectMany = true;
                } else if (genericReturnType instanceof Class) {
                    returnType = (Class) genericReturnType;
                }

                MappedStatement mappedStatement = MappedStatement.builder()
                        .id(aClass.getName() + "." + method.getName())
                        .sql(originalSql)
                        .returnType(returnType)
                        .sqlCommandType(sqlCommandType)
                        .isSelectMany(isSelectMany)
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
