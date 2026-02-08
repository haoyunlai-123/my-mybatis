package com.my.mybatis.session.defaults;

import com.my.mybatis.session.Configuration;
import com.my.mybatis.session.SqlSession;
import com.my.mybatis.session.SqlSessionFactory;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration, configuration.newExecutor());
    }
}
