package com.my.mybatis.design.proxy._03;

import com.my.demo.entity.User;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxy implements MethodInterceptor {

    /**
     * 返回代理对象
     * @param clazz
     * @return
     */
    public Object getProxy(Class<?> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("start......");
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("end......");
        return result;
    }

    public static void main(String[] args) {
        CglibProxy cglibProxy = new CglibProxy();
        UserService proxy = (UserService) cglibProxy.getProxy(UserService.class);
        System.out.println(proxy.selectList("小鸭哥"));
    }
}
