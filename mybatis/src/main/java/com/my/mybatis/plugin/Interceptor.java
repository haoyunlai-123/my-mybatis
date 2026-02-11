package com.my.mybatis.plugin;

/**
 *
 * 拦截器
 */
public interface Interceptor {

    Object intercept(Invocation invocation);


    <T> T plugin(Object target);
}
