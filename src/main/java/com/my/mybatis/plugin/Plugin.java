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

    // 可优化为map，key为方法所在类，value为该类被拦截的方法集合
    private Set<Method> methods;

    public Plugin(Object target, Interceptor interceptor, Set<Method> methods) {
        this.target = target;
        this.interceptor = interceptor;
        this.methods = methods;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (methods != null && methods.contains(method)) {
            return interceptor.intercept(new Invocation(target, method, args));
        } else {
            return method.invoke(target, args);
        }
    }

    public static <T> T wrap(Object target, Interceptor interceptor) {

        Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
        boolean isProxy = false;
        Set<Method> methods = null;
        for (Class<?> aClass : signatureMap.keySet()) {
            // aClass ==> 当前interceptor能代理的类， target.getClass() ==> 目标类
            if (aClass.isAssignableFrom(target.getClass())) {
                isProxy = true;
                // 若有多个类，后面的会覆盖前面的，暂不考虑一个interceptor能代理多个类的情况
                methods = signatureMap.get(aClass);
                break;
            }
        }

        if (isProxy) {
            // 需要代理
            return (T) Proxy.newProxyInstance(
                    target.getClass().getClassLoader(),
                    target.getClass().getInterfaces(),
                    new Plugin(target, interceptor, methods)
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
        if (intercepts == null) {
            throw new RuntimeException("No @Intercepts annotation found in interceptor " + interceptor.getClass().getName());
        }
        Signature[] signatures = intercepts.value();
        for (Signature signature : signatures) {
            Class<?> type = signature.type();
            String methodName = signature.method();
            Class<?>[] args = signature.args();
            Method method = type.getMethod(methodName, args);

            result.computeIfAbsent(type, key -> new ConcurrentHashSet<>()).add(method);
        }
        return result;
    }
}
