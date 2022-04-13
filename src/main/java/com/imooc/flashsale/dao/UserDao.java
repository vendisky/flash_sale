package com.imooc.flashsale.dao;

import com.imooc.flashsale.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from fs_user where id = #{id}")
    public User getById(@Param("id") long id);
}
