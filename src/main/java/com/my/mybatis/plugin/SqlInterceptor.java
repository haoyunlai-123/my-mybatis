package com.my.mybatis.plugin;

import com.my.mybatis.executor.Executor;
import com.my.mybatis.mapping.MappedStatement;

@Intercepts(
        {@Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class}
        )}
)
public class SqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
        System.out.println("SqlInterceptor intercept start...");
        Object result = invocation.proceed();
        System.out.println("SqlInterceptor intercept end...");
        return result;
    }

    @Override
    public <T> T plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
