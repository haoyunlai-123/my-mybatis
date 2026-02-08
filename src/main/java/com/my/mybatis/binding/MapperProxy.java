package com.my.mybatis.binding;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.my.demo.entity.User;
import com.my.demo.mapper.UserMapper;
import com.my.mybatis.annotation.Param;
import com.my.mybatis.annotation.Select;
import com.my.mybatis.builder.XMLConfigBuilder;
import com.my.mybatis.executor.Executor;
import com.my.mybatis.executor.SimpleExecutor;
import com.my.mybatis.mapping.MappedStatement;
import com.my.mybatis.parsing.GenericTokenParser;
import com.my.mybatis.parsing.ParameterMappingTokenHandler;
import com.my.mybatis.session.Configuration;
import com.my.mybatis.session.DefaultSqlSession;
import com.my.mybatis.session.SqlSession;
import com.my.mybatis.type.IntegerTypeHandler;
import com.my.mybatis.type.StringTypeHandler;
import com.my.mybatis.type.TypeHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class MapperProxy implements InvocationHandler {

    private SqlSession sqlSession;

    private Class mapperClass;


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 建立mapper层方法参数名称和值的映射
        Map<String,Object> paramValueMap = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Param param = parameter.getAnnotation(Param.class);
            if (param == null) { continue; }
            String paramName = param.value();
            paramValueMap.put(paramName, args[i]);
        }

        String statementId = mapperClass.getName() + "." + method.getName();
        return sqlSession.selectList(statementId, paramValueMap);

    }



    public static void main(String[] args) {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parse();

        SqlSession sqlsession = new DefaultSqlSession(configuration, configuration.newExecutor());
        UserMapper userMapper = sqlsession.getMapper(UserMapper.class);

        List<User> zq = userMapper.selectList(1, "zq");
        System.out.println(JSONUtil.toJsonStr(zq));
    }
}
