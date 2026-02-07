package com.my.mybatis.design.proxy._01;

public class UserProxy implements UserService {

    private UserService userService;

    public UserProxy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object selectList(String name) {
        System.out.println("代理类执行了start........");
        Object list = userService.selectList(name);
        System.out.println("代理类执行了end........");
        return list;
    }

    @Override
    public Object selectOne(String name) {
        System.out.println("代理类执行了start........");
        Object one = userService.selectOne(name);
        System.out.println("代理类执行了end........");
        return one;
    }
}
