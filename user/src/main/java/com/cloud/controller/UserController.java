package com.cloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.DTO.UserDTO;
import com.cloud.entity.Users;
import com.cloud.service.UsersService;
import com.cloud.utils.JwtUtil;
import com.cloud.utils.R;
import com.cloud.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户模块
 *
 * @author 阿东
 * 2024-01-28
 **/
@RestController
@Controller
public class UserController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private RedisCache redisCache;

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
                String jwt = JwtUtil.createJWT(user.getUsername());
                redisCache.setCacheObject("User:"+user.getUsername(),jwt);
                return R.success("登录成功",jwt);
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


    @GetMapping("/userInfo")
    public R<Object> getUserInfo(HttpServletRequest request){
        String username = JwtUtil.getUsername(request);
        LambdaQueryWrapper<Users> users=new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Users> eq = users.eq(Users::getUsername, username);
        Users one = usersService.getOne(eq);
        return  R.success(one);
    }



}
