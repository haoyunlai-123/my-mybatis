package com.my.mybatis.scripting;

import com.my.mybatis.parsing.BindingTokenHandler;
import com.my.mybatis.parsing.GenericTokenParser;
import com.my.mybatis.parsing.ParameterMappingTokenHandler;
import lombok.SneakyThrows;

import java.util.List;

/**
 * 文本sql节点
 */
public class TextSqlNode implements SqlNode {

    private String text; // select * from t_user where id = ${id}

    public TextSqlNode(String test) {
        this.text = test;
    }

    @SneakyThrows
    @Override
    public void apply(DynamicContext context) {
        BindingTokenHandler handler = new BindingTokenHandler(context);
        GenericTokenParser genericTokenParser = new GenericTokenParser("${", "}", handler);
        String sql = genericTokenParser.parse(text);
        context.appendSql(sql);

    }
}
