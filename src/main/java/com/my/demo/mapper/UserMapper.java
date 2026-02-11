package com.my.demo.mapper;

import com.google.common.primitives.Ints;
import com.my.mybatis.annotation.*;
import com.my.demo.entity.User;

import java.util.List;

@CacheNamespace
public interface UserMapper {

    User findOne(@Param("id")Integer id);

    @Select("select * from t_user")
    List<User> selectList();

    @Select("select * from t_user where id = #{id}")
    User selectOne(@Param("id") Integer id);

    @Insert("insert into t_user(name, age) values(#{user.name}, #{user.age})")
    Long insert(@Param("user") User user);

    @Update("update t_user set name = #{user.name}, age = #{user.age} where id = #{user.id}")
    Integer update(@Param("user") User user);

    /*@Update("update t_user set name = #{name}, age = #{age} where id = #{id}")
    Integer update(@Param("name") String name, @Param("age") Integer age, @Param("id") Integer id);
*/
    @Delete("delete from t_user where id = #{id}")
    Integer delete(@Param("id") Integer id);
}
