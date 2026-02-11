package com.my.mybatis.session;

import com.my.mybatis.builder.XMLConfigBuilder;
import com.my.mybatis.session.defaults.DefaultSqlSessionFactory;

/**
 * SqlSession工厂建造者
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory builder() {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parse();
        DefaultSqlSessionFactory sessionFactory = new DefaultSqlSessionFactory(configuration);
        return sessionFactory;
    }

}
