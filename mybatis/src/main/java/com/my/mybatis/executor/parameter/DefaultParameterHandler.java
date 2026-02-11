package com.my.mybatis.executor.parameter;

import cn.hutool.core.util.ReflectUtil;
import com.my.mybatis.session.Configuration;
import com.my.mybatis.type.TypeHandler;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

/**
 * 默认参数处理器
 */
public class DefaultParameterHandler implements ParameterHandler {

    private final Configuration configuration;

    public DefaultParameterHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @SneakyThrows
    @Override
    public void setParam(PreparedStatement ps, List<String> parameterMappings, Object parameter) {
        Map<Class, TypeHandler> typeHandlerMap = configuration.getTypeHandlerMap();
        // 根据映射给sql参数赋值
        Map<String, Object> paramValueMap = (Map<String, Object>) parameter;
        // paramValueMap: "user" ==> user对象
        for (int i = 0; i < parameterMappings.size(); i++) {
            String jdbcColumnName = parameterMappings.get(i);
            Object val = paramValueMap.get(jdbcColumnName);

            // user.name ==> name
            // 反射获取user对象的属性值
            if (jdbcColumnName.contains(".")) {
                String[] split = jdbcColumnName.split("\\.");
                String objectName = split[0];
                String fieldName = split[1];
                Object objectVal = paramValueMap.get(objectName);
                val = ReflectUtil.getFieldValue(objectVal, fieldName);
            }

            TypeHandler typeHandler = typeHandlerMap.get(val.getClass());
            if (typeHandler == null) {
                ps.setObject(i + 1, val);
            } else {
                typeHandler.setParameter(ps, i + 1, val);
            }

        }
    }
}
