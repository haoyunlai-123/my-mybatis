package com.my.mybatis.session.defaults;

import com.my.mybatis.session.Configuration;
import com.my.mybatis.session.SqlSession;
import com.my.mybatis.session.SqlSessionFactory;
import com.my.mybatis.transaction.JdbcTransaction;
import com.my.mybatis.transaction.Transaction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    @Override
    public SqlSession openSession() {
        return openSession(true);
    }

    @Override
    public SqlSession openSession(boolean autoCommit) {
        Transaction transaction = new JdbcTransaction(configuration.getDataSource(), autoCommit);
        return new DefaultSqlSession(configuration, configuration.newExecutor(transaction));
    }
}
