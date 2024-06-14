package com.ohx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ohx.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    

    int selectMaxId();
}
