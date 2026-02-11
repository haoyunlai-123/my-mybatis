package com.my.mybatis;

import lombok.SneakyThrows;
import ognl.Ognl;
import org.junit.Test;

import java.util.HashMap;

public class TestOgnl {

    @SneakyThrows
    @Test
    public void test() {

        HashMap<String, Object> context = new HashMap<>();
        context.put("name", "猴宝");
        context.put("age", 18);

        // 从上下文中获取值
        String name = (String) Ognl.getValue("name", context);
        Integer age = (Integer) Ognl.getValue("age", context);

        System.out.println(name);
        System.out.println(age);

    }

}
