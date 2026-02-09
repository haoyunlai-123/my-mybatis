package com.my.mybatis.plugin;

import cn.hutool.core.collection.ConcurrentHashSet;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Plugin implements InvocationHandler {

    private Object target;

    private Interceptor interceptor;

    public Plugin(Object target, Interceptor interceptor) {
        this.target = target;
        this.interceptor = interceptor;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return interceptor.intercept(new Invocation(target, method, args));
    }

    public static <T> T wrap(Object target, Interceptor interceptor) {

        if (target.getClass().isAssignableFrom(null)) {
            // 需要代理
            return (T) Proxy.newProxyInstance(
                    target.getClass().getClassLoader(),
                    target.getClass().getInterfaces(),
                    new Plugin(target, interceptor)
            );
        } else {
            // 不需要代理
            return (T) target;
        }
    }

    /**
     * 返回要拦截的类和方法的映射
     * @param interceptor
     * @return
     */
    @SneakyThrows
    private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
        Map<Class<?>, Set<Method>> result = new ConcurrentHashMap<>();
        Intercepts intercepts = interceptor.getClass().getAnnotation(Intercepts.class);
        Signature[] signatures = intercepts.value();
        for (Signature signature : signatures) {
            Class<?> type = signature.type();
            String methodName = signature.method();
            Class<?>[] args = signature.args();
            Method method = type.getMethod(methodName, args);

            result.computeIfAbsent(type, key -> new ConcurrentHashSet<>()).add(method);
        }
    }
}
