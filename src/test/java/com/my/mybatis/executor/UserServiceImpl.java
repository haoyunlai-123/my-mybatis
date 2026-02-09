package com.my.mybatis.executor;

public class UserServiceImpl implements UserService {

    @Override
    public Object selectList(String name) {
        System.out.println("执行了selectList方法");
        return "ok";
    }

    @Override
    public Object selectOne(String name) {
        System.out.println("执行了selectOne方法");
        return "ok";
    }
}
