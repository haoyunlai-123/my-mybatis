package com.my.mybatis.plugin;

import com.my.mybatis.executor.Executor;
import com.my.mybatis.executor.statement.PreparedStatementHandler;
import com.my.mybatis.executor.statement.StatementHandler;
import com.my.mybatis.mapping.BoundSql;
import com.my.mybatis.mapping.MappedStatement;

import java.sql.Connection;

@Intercepts(
        {@Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class}
        )}
)
public class LimitInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
        /*System.out.println("LimitInterceptor intercept start...");
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        if (! ms.getSql().contains("LIMIT")) {
            // 已经有limit了，不再添加
            ms.setSql(ms.getSql() + " LIMIT 2");
        }
        Object result = invocation.proceed();
        System.out.println("LimitInterceptor intercept end...");*/
        PreparedStatementHandler psh = (PreparedStatementHandler)  invocation.getTarget();
        BoundSql boundSql = psh.getBoundSql();
        String sql = boundSql.getSql();
        if (! sql.contains("LIMIT")) {
            boundSql.setSql(sql + " LIMIT 2");
        }
        Object result = invocation.proceed();
        return result;
    }

    @Override
    public <T> T plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
