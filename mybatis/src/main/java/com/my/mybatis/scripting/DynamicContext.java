package com.my.mybatis.scripting;

import java.util.Map;
import java.util.StringJoiner;

/**
 * 上下文对象
 */
public class DynamicContext {

    private Map<String, Object> bindings;

    private StringJoiner sqlBuilder = new StringJoiner(" ");

    public DynamicContext(Map<String, Object> bindings) {
        this.bindings = bindings;
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void appendSql(String sqlTest) {
        sqlBuilder.add(sqlTest.trim());
    }

    public String getSql() {
        return sqlBuilder.toString();
    }
}
