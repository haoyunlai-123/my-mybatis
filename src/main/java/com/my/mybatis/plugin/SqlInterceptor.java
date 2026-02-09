package com.my.mybatis.plugin;

public class SqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
        System.out.println("SqlInterceptor intercept start...");
        Object result = invocation.proceed();
        System.out.println("SqlInterceptor intercept end...");
        return result;
    }
}
