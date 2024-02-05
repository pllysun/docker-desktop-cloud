package com.cloud.mapper;

import com.cloud.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author rongshan
* @description 针对表【users(用户表)】的数据库操作Mapper
* @createDate 2024-01-28 22:13:22
* @Entity com.cloud.entity.Users
*/
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}




