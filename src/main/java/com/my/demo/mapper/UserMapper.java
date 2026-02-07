package com.my.demo.mapper;

import com.my.mybatis.annotation.Select;
import com.my.demo.entity.User;

import java.util.List;

public interface UserMapper {

    @Select("select * from t_user")
    List<User> selectList();

}
