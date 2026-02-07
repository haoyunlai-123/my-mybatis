package com.my.mybatis;

import cn.hutool.core.util.ReflectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TestReflect {

    @Test
    public void test() throws Exception{
        Class<User> userClass = User.class;
        Field name = userClass.getDeclaredField("name");
        User user = userClass.newInstance();
        name.setAccessible(true);
        name.set(user, "小鸭哥");
        System.out.println(user);

        Method method = userClass.getDeclaredMethod("hello");
        method.invoke(user);
    }

    @Test
    public void test1() throws Exception{
        Field field = ReflectUtil.getField(User.class, "name");
        field.setAccessible(true);
        System.out.println(field.get(null));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class User {
        private String name;

        private Integer age;

        public void hello() {
            System.out.println("hello");
        }
    }
}
