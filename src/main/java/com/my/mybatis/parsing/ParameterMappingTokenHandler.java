package com.my.mybatis.parsing;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParameterMappingTokenHandler implements TokenHandler {

    private List<String> parameterMappings = Lists.newArrayList();

    /**
     * 处理标记
     *
     * @param token 参数名称
     * @return 解析后的替代内容 name -> ?
     */
    @Override
    public String handleToken(String token) {
        parameterMappings.add(token);
        return "?";
    }
}
