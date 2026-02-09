package com.my.mybatis.plugin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * 代理对象执行所需参数
 */
@Data
@AllArgsConstructor
public class Invocation {

    private final Object target; // 目标对象

    private final Method method; // 目标方法

    private final Object[] args; // 目标方法参数

    @SneakyThrows
    public Object proceed() {
        return method.invoke(target, args);
    }
}
