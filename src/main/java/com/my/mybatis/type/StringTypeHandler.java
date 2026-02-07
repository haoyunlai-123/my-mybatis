package com.my.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StringTypeHandler implements  TypeHandler<String> {

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter) throws SQLException {
        ps.setString(i, parameter);
    }
}
