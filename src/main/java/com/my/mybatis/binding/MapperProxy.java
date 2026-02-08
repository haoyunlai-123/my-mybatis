package com.my.mybatis.binding;

import cn.hutool.json.JSONUtil;
import com.my.demo.entity.User;
import com.my.demo.mapper.UserMapper;
import com.my.mybatis.annotation.Param;
import com.my.mybatis.builder.XMLConfigBuilder;
import com.my.mybatis.mapping.MappedStatement;
import com.my.mybatis.mapping.SqlCommandType;
import com.my.mybatis.session.Configuration;
import com.my.mybatis.session.SqlSessionFactory;
import com.my.mybatis.session.SqlSessionFactoryBuilder;
import com.my.mybatis.session.defaults.DefaultSqlSession;
import com.my.mybatis.session.SqlSession;
import com.my.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.sun.jndi.cosnaming.CNCtxFactory;
import lombok.AllArgsConstructor;

import java.lang.reflect.*;
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
        MappedStatement mp = sqlSession.getConfiguration().getMappedStatement(statementId);
        SqlCommandType sqlCommandType = mp.getSqlCommandType();

        switch (sqlCommandType) {
            case INSERT:
                return sqlSession.insert(statementId, paramValueMap);
            case UPDATE:
                return sqlSession.update(statementId, paramValueMap);
            case DELETE:
                return sqlSession.delete(statementId, paramValueMap);
            case SELECT:
                /*Type returnType = method.getGenericReturnType();
                if (returnType instanceof ParameterizedType) {
                    // 返回值是泛型类型
                    return sqlSession.selectList(statementId, paramValueMap);
                } else {
                    // 返回值不是泛型类型
                    return sqlSession.selectOne(statementId, paramValueMap);
                }*/
                Boolean isSelectMany = mp.getIsSelectMany();
                if (isSelectMany) {
                    return sqlSession.selectList(statementId, paramValueMap);
                } else {
                    return sqlSession.selectOne(statementId, paramValueMap);
                }
            default:
                throw new RuntimeException("Unknown SqlCommandType: " + sqlCommandType);
        }

    }



    public static void main(String[] args) {
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().builder();
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        /*List<User> zq = userMapper.selectList(1, "zq");
        System.out.println(JSONUtil.toJsonStr(zq));*/
        System.out.println(userMapper.update("暗月骑士", 1, 1));
    }
}
