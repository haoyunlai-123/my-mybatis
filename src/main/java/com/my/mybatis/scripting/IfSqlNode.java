package com.my.mybatis.scripting;

import lombok.SneakyThrows;
import ognl.Ognl;

import java.util.Map;

/**
 * if标签的sql节点
 */
public class IfSqlNode implements SqlNode {

    private String test;

    private SqlNode sqlNode; // 下个sql节点

    public IfSqlNode(String test, SqlNode sqlNode) {
        this.test = test;
        this.sqlNode = sqlNode;
    }

    @SneakyThrows
    @Override
    public void apply(Map context) {
        Boolean value = (Boolean) Ognl.getValue(test, context);
        if (value) {
            sqlNode.apply(context);
        }
    }
}
