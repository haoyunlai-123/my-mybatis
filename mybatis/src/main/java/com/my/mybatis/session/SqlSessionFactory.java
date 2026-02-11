package com.my.mybatis.session;

/**
 * 生产SqlSession
 */
public interface SqlSessionFactory {

    SqlSession openSession();

    SqlSession openSession(boolean autoCommit);

}
