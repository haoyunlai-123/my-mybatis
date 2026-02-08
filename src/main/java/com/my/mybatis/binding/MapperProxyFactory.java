package com.my.mybatis.binding;

import com.my.mybatis.session.Configuration;
import com.my.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

public class MapperProxyFactory {


    public static <T> T getProxy(
            Class<T> mapperClass,
            SqlSession sqlSession
    ) {
        return (T) Proxy.newProxyInstance(
                mapperClass.getClassLoader(),
                new Class[]{mapperClass},
                new MapperProxy(sqlSession, mapperClass)
        );
    }

}
