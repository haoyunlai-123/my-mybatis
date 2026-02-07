package com.my.demo.mapper;

import com.my.mybatis.annotation.Param;
import com.my.mybatis.annotation.Select;
import com.my.demo.entity.User;

import java.util.List;

public interface UserMapper {

    @Select("select * from t_user where id = #{id} and name = #{name}")
    List<User> selectList(@Param("id") Integer id, @Param("name") String name);

    @Select("select * from t_user where id = #{id}")
    User selectOne(@Param("id") Integer id);
}
