package com.my.demo.mapper;

import com.google.common.primitives.Ints;
import com.my.mybatis.annotation.*;
import com.my.demo.entity.User;

import java.util.List;

public interface UserMapper {

    @Select("select * from t_user where id = #{id} and name = #{name}")
    List<User> selectList(@Param("id") Integer id, @Param("name") String name);

    @Select("select * from t_user where id = #{id}")
    User selectOne(@Param("id") Integer id);

    @Insert("insert into t_user(name, age) values(#{user.name}, #{user.age})")
    Integer insert(@Param("user") User user);

    @Update("update t_user set name = #{user.name}, age = #{user.age} where id = #{user.id}")
    Integer update(@Param("user") User user);

    @Delete("delete from t_user where id = #{id}")
    Integer delete(@Param("id") Integer id);
}
