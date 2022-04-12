package com.imooc.flashsale.dao;

import com.imooc.flashsale.domain.TestData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestDataDao {
    @Select("select * from test where id = #{id}")
    public TestData getById(@Param("id") int id);
}
