package com.my.mybatis.binding;

import com.my.mybatis.session.Configuration;

import java.lang.reflect.Proxy;

public class MapperProxyFactory {


    public static <T> T getProxy(
            Class<T> mapperClass,
            Configuration configuration
    ) {
        return (T) Proxy.newProxyInstance(
                mapperClass.getClassLoader(),
                new Class[]{mapperClass},
                new MapperProxy(configuration, mapperClass)
        );
    }

}
