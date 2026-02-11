package com.my.mybatis.parsing;

import com.google.common.collect.Lists;
import com.my.mybatis.scripting.DynamicContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import ognl.Ognl;

import java.util.List;

/**
 * 参数处理器
 */
@Data
@AllArgsConstructor
public class BindingTokenHandler implements TokenHandler {

    private DynamicContext context;

    @SneakyThrows
    @Override
    public String handleToken(String content) {
        return String.valueOf(Ognl.getValue(content, context.getBindings()));
    }

}
