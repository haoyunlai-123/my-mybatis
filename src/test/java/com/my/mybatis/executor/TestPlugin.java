package com.my.mybatis.executor;

import cn.hutool.json.JSONUtil;
import com.my.demo.entity.User;
import com.my.demo.mapper.UserMapper;
import com.my.mybatis.plugin.Interceptor;
import com.my.mybatis.plugin.LimitInterceptor;
import com.my.mybatis.plugin.Plugin;
import com.my.mybatis.plugin.SqlInterceptor;
import com.my.mybatis.session.SqlSession;
import com.my.mybatis.session.SqlSessionFactory;
import com.my.mybatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.util.Arrays;

public class TestPlugin {

    @Test
    public void test1() {
       /* UserService userService = Plugin.<UserService>wrap(
                new UserServiceImpl(),
                Arrays.<Interceptor>asList(
                        new SqlInterceptor(),
                        new LimitInterceptor()
                    )
                );*/

        /*UserService userService = Plugin.<UserService>wrap(new UserServiceImpl(), new SqlInterceptor());
        UserService userService1 = Plugin.<UserService>wrap(userService, new LimitInterceptor());
        System.out.println(userService1.selectOne("猴宝"));*/

        LimitInterceptor limitInterceptor = new LimitInterceptor();
        Object limitPlugin = limitInterceptor.plugin(new UserServiceImpl());

        SqlInterceptor sqlInterceptor = new SqlInterceptor();
        UserService userService = sqlInterceptor.plugin(limitPlugin);

        System.out.println(userService.selectOne("猴宝"));
    }

    @Test
    public void test2() {
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().builder();
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        System.out.println(JSONUtil.toJsonStr(userMapper.insert(User.builder().name("巨剑").age(1000).build())));
    }

}
