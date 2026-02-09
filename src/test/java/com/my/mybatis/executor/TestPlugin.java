package com.my.mybatis.executor;

import com.my.mybatis.plugin.Interceptor;
import com.my.mybatis.plugin.LimitInterceptor;
import com.my.mybatis.plugin.Plugin;
import com.my.mybatis.plugin.SqlInterceptor;
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
        UserService userService = Plugin.<UserService>wrap(new UserServiceImpl(), new SqlInterceptor());
        UserService userService1 = Plugin.<UserService>wrap(userService, new LimitInterceptor());
        System.out.println(userService1.selectOne("猴宝"));
    }

}
