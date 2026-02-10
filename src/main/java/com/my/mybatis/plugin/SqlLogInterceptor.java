package com.my.mybatis.plugin;

import com.my.mybatis.executor.statement.StatementHandler;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * SQL日志插件
 */
@Intercepts(
        {@Signature(
                type = StatementHandler.class,
                method = "query",
                args = {Statement.class}
        ), @Signature(
                type = StatementHandler.class,
                method = "update",
                args = {Statement.class}
        ),@Signature(
                type = StatementHandler.class,
                method = "insert",
                args = {Statement.class}
        ),@Signature(
                type = StatementHandler.class,
                method = "delete",
                args = {Statement.class}
        )}
)
@Slf4j
public class SqlLogInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
        System.out.println("SqlInterceptor intercept start...");
        PreparedStatement ps = (PreparedStatement) invocation.getArgs()[0];
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
