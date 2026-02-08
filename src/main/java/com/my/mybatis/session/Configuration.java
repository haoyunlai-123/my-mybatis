package com.my.mybatis.session;

import com.my.mybatis.executor.Executor;
import com.my.mybatis.executor.SimpleExecutor;
import com.my.mybatis.mapping.MappedStatement;
import com.my.mybatis.type.IntegerTypeHandler;
import com.my.mybatis.type.StringTypeHandler;
import com.my.mybatis.type.TypeHandler;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 核心配置
 */
@Data
public class Configuration {

    private Map<Class, TypeHandler> typeHandlerMap = new HashMap<>();

    {
        typeHandlerMap.put(Integer.class, new IntegerTypeHandler());
        typeHandlerMap.put(String.class, new StringTypeHandler());
    }

    // key: com.my.demo.mapper.UserMapper.selectOne <===> value: MappedStatement
    private Map<String, MappedStatement> mappedStatementMap =  new HashMap<>();

    public void addMappedStatement(MappedStatement ms) {
        mappedStatementMap.put(ms.getId(), ms);
    }

    public  MappedStatement getMappedStatement(String id) {
        return mappedStatementMap.get(id);
    }

    public Executor newExecutor() {
        return new SimpleExecutor(this);
    }
}
