package com.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.entity.Users;
import com.cloud.service.UsersService;
import com.cloud.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author rongshan
* @description 针对表【users(用户表)】的数据库操作Service实现
* @createDate 2024-01-28 22:13:22
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}




