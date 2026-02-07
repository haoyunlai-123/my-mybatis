package com.my.mybatis.session;

import com.my.mybatis.mapping.MappedStatement;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 核心配置
 */
@Data
public class Configuration {

    // key: com.my.demo.mapper.UserMapper.selectOne <===> value: MappedStatement
    private Map<String, MappedStatement> mappedStatementMap =  new HashMap<>();

    public void addMappedStatement(MappedStatement ms) {
        mappedStatementMap.put(ms.getId(), ms);
    }

    public  MappedStatement getMappedStatement(String id) {
        return mappedStatementMap.get(id);
    }
}
