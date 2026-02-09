package com.my.mybatis.plugin;

public class LimitInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
        System.out.println("LimitInterceptor intercept start...");
        Object result = invocation.proceed();
        System.out.println("LimitInterceptor intercept end...");
        return result;
    }

    @Override
    public <T> T plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
