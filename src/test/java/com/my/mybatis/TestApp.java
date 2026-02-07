package com.my.mybatis;

import com.my.demo.entity.User;
import com.my.demo.mapper.UserMapper;
import org.junit.Test;

import java.util.List;

public class TestApp {

    @Test
    public void test() throws InstantiationException, IllegalAccessException {
        UserMapper userMapper = UserMapper.class.newInstance();
        List<User> users = userMapper.selectList();
        System.out.println(users);
    }

}
