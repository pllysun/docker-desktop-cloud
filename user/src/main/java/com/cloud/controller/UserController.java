package com.cloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.DTO.UserDTO;
import com.cloud.entity.Users;
import com.cloud.service.UsersService;
import com.cloud.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 登录接口
     * @param user 用户名和密码
     * @return 登录结果
     */
    @PostMapping("/login")
    public R<Object> login(@RequestBody UserDTO user){
        LambdaQueryWrapper<Users> lambda = new QueryWrapper<Users>().lambda();
        lambda.eq(Users::getUsername, user.getUsername());
        Users users = usersService.getOne(lambda);
        if(users!=null){
            if(users.getPassword().equals(user.getPassword())){
                return R.success("登录成功");
            }
            return R.fail("密码错误");
        }
        return R.fail("用户不存在");
    }

    /**
     * 注册用户
     * @param user 用户名和密码
     * @return 注册结果
     */
    @PostMapping("/register")
    public R<Object> register(@RequestBody UserDTO user){
        LambdaQueryWrapper<Users> lambda = new QueryWrapper<Users>().lambda();
        lambda.eq(Users::getUsername, user.getUsername());
        Users users = usersService.getOne(lambda);
        if(users!=null){
            return R.fail("用户名已存在");
        }
        Users userEntity = new Users(user.getUsername(), user.getPassword());
        usersService.save(userEntity);
        return R.success("注册成功，请登录");
    }


}
