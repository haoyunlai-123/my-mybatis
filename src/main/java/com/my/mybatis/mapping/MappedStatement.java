package com.my.mybatis.mapping;

import com.my.mybatis.parsing.GenericTokenParser;
import com.my.mybatis.parsing.ParameterMappingTokenHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * mapper配置信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MappedStatement {

    private String id; // mapper方法唯一标识: com.my.demo.mapper.UserMapper.selectOne

    private String sql; // SQL

    private Class returnType; // 返回类型，若为泛型类型，则为泛型参数的类型

    private SqlCommandType sqlCommandType; // SQL类型：SELECT、INSERT、UPDATE、DELETE

    private Boolean isSelectMany; // 是否为查询多条记录的SELECT语句

    public BoundSql getBoundSql() {
        // sql解析： #{}
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler();
        GenericTokenParser tokenParser = new GenericTokenParser("#{", "}", handler);
        String sql = tokenParser.parse(this.sql);
        List<String> parameterMappings = handler.getParameterMappings();
        return new BoundSql(sql, parameterMappings);
    }

    public String getCacheKey(Object parameter) {
        return id + ":" + sql + ":" + parameter;
    }
}
