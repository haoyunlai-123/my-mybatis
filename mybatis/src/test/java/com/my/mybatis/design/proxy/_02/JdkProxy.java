package com.my.mybatis.design.proxy._02;

import com.my.mybatis.design.proxy._01.UserService;
import com.my.mybatis.design.proxy._01.UserServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxy implements InvocationHandler {

    private Object target;

    public JdkProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        /*UserService proxy = (UserService) Proxy.newProxyInstance(
                userService.getClass().getClassLoader(),
                new Class[]{UserService.class},
                new JdkProxy(userService)
        );*/
        JdkProxy jdkProxy = new JdkProxy(new UserServiceImpl());
        UserService proxy = jdkProxy.getProxy(UserService.class);
        System.out.println(proxy.selectList("小鸭哥"));
    }
}
