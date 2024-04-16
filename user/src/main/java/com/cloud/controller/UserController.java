package com.cloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.DTO.UserDTO;
import com.cloud.DTO.UserInfoDTO;
import com.cloud.entity.Occupation;
import com.cloud.entity.Personalise;
import com.cloud.entity.Role;
import com.cloud.entity.Users;
import com.cloud.service.OccupationService;
import com.cloud.service.PersonaliseService;
import com.cloud.service.RoleService;
import com.cloud.service.UsersService;
import com.cloud.utils.JwtUtil;
import com.cloud.utils.R;
import com.cloud.utils.RedisCache;
import com.cloud.vo.UserInfoVo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private OccupationService occupationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PersonaliseService personaliseService;

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

    /**
     * 获取用户信息(单个用户信息)
     * @return 用户信息
     */
    @GetMapping("/userInfo")
    public R<Object> getUserInfo(HttpServletRequest request){
        String username = JwtUtil.getUsername(request);
        LambdaQueryWrapper<Users> users=new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Users> eq = users.eq(Users::getUsername, username);
        Users one = usersService.getOne(eq);
        LambdaQueryWrapper<Occupation> occupation = new QueryWrapper<Occupation>().lambda();
        occupation.eq(Occupation::getOccupationId, one.getOccupationId());
        Occupation one1 = occupationService.getOne(occupation);
        LambdaQueryWrapper<Role> role=new LambdaQueryWrapper<>();
        role.eq(Role::getRoleId, one.getRoleId());
        Role one2 = roleService.getOne(role);
        UserInfoVo userInfoVo = new UserInfoVo(one,one1.getOccupationName(),one2.getRoleName());
        return  R.success(userInfoVo);
    }

    /**
     * 获取用户列表
     * @return 用户列表
     */
    @GetMapping("/userList")
    public R<Object> getUserList(@RequestParam("page") int page, @RequestParam("limit") int limit){
        IPage<Users> ipage =  new Page<>(page,limit);
        List<Users> records = usersService.page(ipage).getRecords();
        return R.success(records);
    }


    /**
     * 修改用户密码
     * @param user 用户名和新密码
     * @return 修改结果
     */
    @PostMapping("/updatePassword")
    public R<Object> updateUser(@RequestBody UserDTO user){
        LambdaQueryWrapper<Users> lambda = new QueryWrapper<Users>().lambda();
        lambda.eq(Users::getUsername, user.getUsername());
        Users users = usersService.getOne(lambda);
        if(users!=null){
            users.setPassword(user.getPassword());
            boolean update = usersService.updateById(users);
            if(update){
                return R.success("修改成功");
            }
            return R.fail("修改失败");
        }
        return R.fail("用户不存在");
    }

    /**
     * 退出登录
     * @return 退出结果
     */
    @GetMapping("/logout")
    public R<Object> logout(HttpServletRequest request){
        String username = JwtUtil.getUsername(request);
        redisCache.deleteObject("User:"+username);
        return R.success("退出成功");
    }

    /**
     * 获取用户数量
     * @return 用户数量
     */
    @GetMapping("/userCount")
    public R<Object> userCount(){
        int count = (int) usersService.count();
        return R.success(count);
    }

    /**
     * 修改（添加）用户信息
     * 修改什么信息传什么信息的数据，如果不修改不传或者为null即可
     * @param user 用户信息
     * @return
     */
    @PostMapping("/userInfo")
    public R<Object> getUserInfo(@RequestBody UserInfoDTO user,HttpServletRequest request){
        String username = JwtUtil.getUsername(request);
        return updateUserInfo(user, username);
    }

    @NotNull
    private R<Object> updateUserInfo(UserInfoDTO user, String username) {
        LambdaQueryWrapper<Users> users=new LambdaQueryWrapper<>();
        users.eq(Users::getUsername, username);
        Users one = usersService.getOne(users);
        if(user.getOccupationId()!=null){
            one.setOccupationId(Integer.valueOf(user.getOccupationId()));
        }
        if(user.getPhone()!=null){
            one.setPhone(user.getPhone());
        }
        if(user.getEmail()!=null){
            one.setEmail(user.getEmail());
        }
        if(user.getRoleId()!=null){
            one.setRoleId(Integer.valueOf(user.getRoleId()));
        }
        boolean update = usersService.updateById(one);
        if(update){
            return R.success("修改成功");
        }
        return R.fail("修改失败");
    }

    /**
     * 管理员修改用户信息
     * @param user
     * @return
     */
    @PostMapping("/userInfo/management")
    public R<Object> getUserInfo(UserInfoDTO user){
       return updateUserInfo(user,user.getName());
    }


    /**
     * 获取职位信息
     * @return 职位信息
     */
    @GetMapping("/getOccupation")
    public R<Object> getOccupation(){
        List<Occupation> list = occupationService.list();
        return R.success(list);
    }
    /**
     * 获取个性化信息
     * @return 个性化信息
     */
    @GetMapping("/personaliseList")
    public R<Object> personaliseList(){
        List<Personalise> list = personaliseService.list();
        return R.success(list);
    }
}
