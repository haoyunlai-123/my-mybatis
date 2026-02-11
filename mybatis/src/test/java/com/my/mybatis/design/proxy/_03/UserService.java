package com.my.mybatis.design.proxy._03;

public class UserService {

    public Object selectList(String name) {
        System.out.println("查询用户列表");
        return "ok";
    }

}
