package com.my.mybatis.scripting;

import lombok.SneakyThrows;
import ognl.Ognl;

import java.util.Map;

/**
 * if标签的sql节点
 */
public class IfSqlNode implements SqlNode {

    private String text;

    private SqlNode sqlNode; // 下个sql节点

    public IfSqlNode(String text, SqlNode sqlNode) {
        this.text = text;
        this.sqlNode = sqlNode;
    }

    @SneakyThrows
    @Override
    public void apply(DynamicContext context) {
        Boolean value = (Boolean) Ognl.getValue(text, context.getBindings());
        if (value) {
            sqlNode.apply(context);
        }
    }
}
