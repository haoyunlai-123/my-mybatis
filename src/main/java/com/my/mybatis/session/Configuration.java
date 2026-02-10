package com.my.mybatis.session;

import com.my.mybatis.executor.CacheExecutor;
import com.my.mybatis.executor.Executor;
import com.my.mybatis.executor.SimpleExecutor;
import com.my.mybatis.datasource.PooledDataSource;
import com.my.mybatis.executor.parameter.DefaultParameterHandler;
import com.my.mybatis.executor.parameter.ParameterHandler;
import com.my.mybatis.executor.resultset.DefaultResultSetHandler;
import com.my.mybatis.executor.resultset.ResultSetHandler;
import com.my.mybatis.executor.statement.PreparedStatementHandler;
import com.my.mybatis.executor.statement.StatementHandler;
import com.my.mybatis.mapping.MappedStatement;
import com.my.mybatis.plugin.InterceptorChain;
import com.my.mybatis.plugin.LimitInterceptor;
import com.my.mybatis.plugin.SqlLogInterceptor;
import com.my.mybatis.transaction.Transaction;
import com.my.mybatis.type.IntegerTypeHandler;
import com.my.mybatis.type.StringTypeHandler;
import com.my.mybatis.type.TypeHandler;
import lombok.Data;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 核心配置
 */
@Data
public class Configuration {

    private final Map<Class, TypeHandler> typeHandlerMap = new HashMap<>();

    // 插件链
    private final InterceptorChain interceptorChain = new InterceptorChain();

    // 连接池
    private final DataSource dataSource = new PooledDataSource();

    private boolean cacheEnabled = true;

    {
        typeHandlerMap.put(Integer.class, new IntegerTypeHandler());
        typeHandlerMap.put(String.class, new StringTypeHandler());
        interceptorChain.addInterceptor(new LimitInterceptor());
        interceptorChain.addInterceptor(new SqlLogInterceptor());
    }

    // key: com.my.demo.mapper.UserMapper.selectOne <===> value: MappedStatement
    private final Map<String, MappedStatement> mappedStatementMap =  new HashMap<>();

    public void addMappedStatement(MappedStatement ms) {
        mappedStatementMap.put(ms.getId(), ms);
    }

    public  MappedStatement getMappedStatement(String id) {
        return mappedStatementMap.get(id);
    }

    public Executor newExecutor(Transaction transaction) {
        Executor executor = new SimpleExecutor(this, transaction);
        if (cacheEnabled) {
            executor = new CacheExecutor(executor);
        }
        return (Executor) interceptorChain.pluginAll(executor);
    }

    public ResultSetHandler newResultSetHandler() {
        return (ResultSetHandler) interceptorChain.pluginAll(new DefaultResultSetHandler(this));
    }

    public ParameterHandler newParameterHandler() {
        return (ParameterHandler) interceptorChain.pluginAll(new DefaultParameterHandler(this));
    }

    public StatementHandler newStatementHandler(
            MappedStatement ms,
            Object parameter
    ) {
        return (StatementHandler) interceptorChain.pluginAll(
                new PreparedStatementHandler(
                        this,
                        ms,
                        parameter
                )
        );
    }
}
