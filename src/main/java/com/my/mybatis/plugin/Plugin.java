package com.my.mybatis.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class Plugin implements InvocationHandler {

    private Object target;

    private List<Interceptor> interceptors;

    public Plugin(Object target, List<Interceptor> interceptors) {
        this.target = target;
        this.interceptors = interceptors;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before method");

        Invocation invocation = new Invocation(target, method, args);
        for (Interceptor interceptor : interceptors) {
            interceptor.intercept(invocation);
        }

//        Object result = method.invoke(target, args);

        System.out.println("after method");
        return "1";
    }

    public static <T> T wrap(T target, List<Interceptor> interceptors) {
        return (T) Proxy.newProxyInstance(
          target.getClass().getClassLoader(),
          target.getClass().getInterfaces(),
          new Plugin(target, interceptors)
        );
    }
}
