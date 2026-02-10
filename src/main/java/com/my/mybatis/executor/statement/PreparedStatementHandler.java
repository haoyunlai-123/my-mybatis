package com.my.mybatis.executor.statement;

import com.my.mybatis.executor.parameter.ParameterHandler;
import com.my.mybatis.executor.resultset.ResultSetHandler;
import com.my.mybatis.mapping.BoundSql;
import com.my.mybatis.mapping.MappedStatement;
import com.my.mybatis.session.Configuration;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class PreparedStatementHandler implements StatementHandler {

    private final Configuration configuration;

    private final MappedStatement ms;

    private final Object parameter;

    private final ParameterHandler parameterHandler;

    private final ResultSetHandler resultSetHandler;

    private final BoundSql boundSql;

    public PreparedStatementHandler(Configuration configuration, MappedStatement mappedStatement, Object parameter) {
        this.configuration = configuration;
        this.ms= mappedStatement;
        this.parameter = parameter;
        parameterHandler = configuration.newParameterHandler();
        resultSetHandler = configuration.newResultSetHandler();
        boundSql = ms.getBoundSql();
    }

    @SneakyThrows
    @Override
    public Statement prepare(Connection connection) {
        return connection.prepareStatement(boundSql.getSql());
    }

    @Override
    public void parameterize(Statement statement) {
        parameterHandler.setParam(
                (PreparedStatement) statement,
                boundSql.getParameterMappings(),
                parameter
        );
    }

    @SneakyThrows
    @Override
    public <T> T query(Statement statement) {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return (T) resultSetHandler.handleResultSets(ms, ps);
    }

    @SneakyThrows
    @Override
    public int update(Statement statement) {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return ps.getUpdateCount();
    }

    @SneakyThrows
    @Override
    public int insert(Statement statement) {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return ps.getUpdateCount();
    }

    @SneakyThrows
    @Override
    public int delete(Statement statement) {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return ps.getUpdateCount();
    }
}
