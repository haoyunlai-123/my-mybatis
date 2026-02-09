package com.my.mybatis.plugin;

import com.my.mybatis.executor.Executor;
import com.my.mybatis.mapping.MappedStatement;

@Intercepts(
        {@Signature(type = Executor.class, method = "query")}
)
public class LimitInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
        System.out.println("LimitInterceptor intercept start...");
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        ms.setSql(ms.getSql() + " LIMIT 2");
        Object result = invocation.proceed();
        System.out.println("LimitInterceptor intercept end...");
        return result;
    }

    @Override
    public <T> T plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
