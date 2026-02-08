package com.my.mybatis.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

}
