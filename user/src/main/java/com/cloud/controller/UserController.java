package com.cloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.entity.Users;
import com.cloud.service.UsersService;
import com.cloud.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UserController
 *
 * @author zhou kai
 * 2024-01-28
 **/
@RestController
@Controller
public class UserController {
    @Autowired
    private UsersService usersService;

    @GetMapping("/hello")
    public R<Object> hello() {
        return R.success("你好");
    }

    // 写一个简单的全查询
    @GetMapping("/get")
    public R<Object> get() {
        List<Users> users = usersService.getBaseMapper().selectList(null); //查询所有
        return R.success(users);
    }

}
