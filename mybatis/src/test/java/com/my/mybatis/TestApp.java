package com.my.mybatis;

import cn.hutool.json.JSONUtil;import com.my.demo.entity.User;
import com.my.demo.mapper.UserMapper;
import com.my.mybatis.binding.MapperProxyFactory;
import com.my.mybatis.session.SqlSession;import com.my.mybatis.session.SqlSessionFactory;import com.my.mybatis.session.SqlSessionFactoryBuilder;import org.junit.Test;

import java.util.List;

public class TestApp {

    /*@Test
    public void test() throws InstantiationException, IllegalAccessException {
        UserMapper userMapper = UserMapper.class.newInstance();
        List<User> users = userMapper.selectList();
        System.out.println(users);
    }*/

    /*@Test
    public void test1() throws InstantiationException, IllegalAccessException {
        UserMapper proxy = MapperProxyFactory.getProxy(UserMapper.class);
        List<User> users = proxy.selectList();
        System.out.println(users);
    }*/

    @Test
    public void test() {
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().builder();
        SqlSession sqlSession = sessionFactory.openSession(false);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        System.out.println(JSONUtil.toJsonStr(userMapper.findOne(1)));
    }

}
