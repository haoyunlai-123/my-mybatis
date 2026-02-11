package com.my.mybatis.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 拦截器签名
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Signature {

    Class<?> type(); // 拦截到的Type

    String method(); // 拦截到的Type的方法

    Class<?>[] args(); // 拦截到的Type的方法的参数列表

}
