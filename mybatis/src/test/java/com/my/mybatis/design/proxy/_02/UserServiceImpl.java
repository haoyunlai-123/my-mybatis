package com.my.mybatis.design.proxy._02;

public class UserServiceImpl implements UserService {

    @Override
    public Object selectList(String name) {
        System.out.println("执行了查询方法");
        return "ok";
    }

    @Override
    public Object selectOne(String name) {
        System.out.println("执行了查询方法");
        return "ok";
    }
}
