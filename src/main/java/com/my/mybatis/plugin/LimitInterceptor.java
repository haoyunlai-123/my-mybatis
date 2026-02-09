package com.my.mybatis.plugin;

public class LimitInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
        System.out.println("LimitInterceptor intercept start...");
        Object result = invocation.proceed();
        System.out.println("LimitInterceptor intercept end...");
        return result;
    }
}
