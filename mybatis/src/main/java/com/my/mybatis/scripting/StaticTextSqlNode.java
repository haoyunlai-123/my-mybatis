package com.my.mybatis.scripting;

import lombok.SneakyThrows;
import ognl.Ognl;

/**
 * 静态文本sql节点
 */
public class StaticTextSqlNode implements SqlNode {

    private String text;

    public StaticTextSqlNode(String test) {
        this.text = test;
    }

    @SneakyThrows
    @Override
    public void apply(DynamicContext context) {
        context.appendSql(text);
    }
}
