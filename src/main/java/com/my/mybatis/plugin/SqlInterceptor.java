package com.my.mybatis.plugin;

import com.my.mybatis.executor.Executor;
import com.my.mybatis.executor.resultset.ResultSetHandler;
import com.my.mybatis.mapping.MappedStatement;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;

@Intercepts(
        {@Signature(
                type = ResultSetHandler.class,
                method = "handleResultSets",
                args = {MappedStatement.class, PreparedStatement.class}
        )}
)
@Slf4j
public class SqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
        System.out.println("SqlInterceptor intercept start...");
        PreparedStatement ps = (PreparedStatement) invocation.getArgs()[1];
        log.info("Executing SQL: {}", ps.toString());
        Object result = invocation.proceed();
        System.out.println("SqlInterceptor intercept end...");
        return result;
    }

    @Override
    public <T> T plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
