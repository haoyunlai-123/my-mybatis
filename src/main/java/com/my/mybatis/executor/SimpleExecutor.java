package com.my.mybatis.executor;

import cn.hutool.core.util.ReflectUtil;
import com.my.mybatis.annotation.Param;
import com.my.mybatis.annotation.Select;
import com.my.mybatis.executor.statement.StatementHandler;
import com.my.mybatis.mapping.BoundSql;
import com.my.mybatis.mapping.MappedStatement;
import com.my.mybatis.parsing.GenericTokenParser;
import com.my.mybatis.parsing.ParameterMappingTokenHandler;
import com.my.mybatis.session.Configuration;
import com.my.mybatis.transaction.Transaction;
import com.my.mybatis.type.TypeHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.lang.reflect.Parameter;
import java.sql.*;
import java.util.*;

/**
 * 简单执行器
 */
@Slf4j
public class SimpleExecutor implements Executor {

    private Configuration configuration;

    private Transaction transaction;

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
    }

    @SneakyThrows
    @Override
    public <T> List<T> query(MappedStatement ms, Object parameter) {
        /*Connection connection = getConnection();

        PreparedStatement ps = execute(ms, parameter, connection);*/

        // 拿到mapper的返回值类型
        // 这段代码用于获取方法的返回类型：
        // - 如果返回类型是带泛型的（如 List<User>)，就取出其第一个泛型参数作为实际返回类型。
        // - 如果返回类型不是泛型（如 User），就直接使用该返回类型
        /*Class returnType = null;
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            returnType =  (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
        } else if (genericReturnType instanceof Class) {
            returnType = (Class) genericReturnType;
        }*/

        /*List<T> list = configuration.newResultSetHandler().handleResultSets(ms, ps);

        connection.close();*/

        StatementHandler statementHandler = configuration.newStatementHandler(ms, parameter);
        Statement statement = prepareStatement(statementHandler);
        return statementHandler.query(statement);

    }

    @SneakyThrows
    @Override
    public int update(MappedStatement ms, Object parameter) {
        StatementHandler statementHandler = configuration.newStatementHandler(ms, parameter);
        Statement statement = prepareStatement(statementHandler);
        return statementHandler.update(statement);
    }

    @SneakyThrows
    @Override
    public int insert(MappedStatement ms, Object parameter) {
        StatementHandler statementHandler = configuration.newStatementHandler(ms, parameter);
        Statement statement = prepareStatement(statementHandler);
        return statementHandler.insert(statement);
    }

    @SneakyThrows
    @Override
    public int delete(MappedStatement ms, Object parameter) {
        StatementHandler statementHandler = configuration.newStatementHandler(ms, parameter);
        Statement statement = prepareStatement(statementHandler);
        return statementHandler.delete(statement);
    }

    @Override
    public void commit() {
        transaction.commit();
    }

    @Override
    public void rollback() {
        transaction.rollback();
    }

    @Override
    public void close() {
        transaction.close();
    }

    /*@SneakyThrows
    private PreparedStatement execute(MappedStatement ms, Object parameter, Connection connection) {

        BoundSql boundSql = ms.getBoundSql();

        PreparedStatement ps = connection.prepareStatement(boundSql.getSql());

        // 给sql参数赋值
        configuration.newParameterHandler().setParam(ps, boundSql.getParameterMappings(), parameter);

        log.info("执行SQL: " + boundSql.getSql());
        log.info("参数: " + parameter);
        ps.execute();

        return ps;
    }*/

    private Statement prepareStatement(StatementHandler statementHandler) {
        Connection connection = getConnection();
        Statement statement = statementHandler.prepare(connection);
        statementHandler.parameterize(statement);
        return statement;
    }

    @SneakyThrows
    private Connection getConnection() {
        /*// 加载数据库驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 建立数据库连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://root:1234@localhost:3306/my-mybatis");
        return connection;*/

        return transaction.getConnection();
    }
}
