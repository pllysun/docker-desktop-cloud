package com.cloud.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.cloud.entity.Users;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfoVo {
    /**
     * 编号
     */
    @TableId
    private Integer userId;

    /**
     * 账号
     */
    private String username;

    /**
     * 头像(地址)
     */
    private String userHead;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;


    /**
     *  职业id
     */
    private Integer occupationId;

    /**
     * 职业名称
     */
    private String occupationName;

    /**
     * 系统角色(与用户在社群中的角色区分) - 0:系统用户 -1:系统管理员
     */
    private Integer roleId;

    /**
     * 系统角色名称
     */
    private String roleName;

    /**
     *  桌面数量
     */
    private Integer NumberOfDesktops;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;

    public UserInfoVo(Users one,String occupationName, String roleName) {
        this.username=one.getUsername();
        this.userId=one.getUserId();
        this.userHead=one.getUserHead();
        this.email=one.getEmail();
        this.phone=one.getPhone();
        this.occupationId=one.getOccupationId();
        this.roleId=one.getRoleId();
        this.NumberOfDesktops=one.getNumberOfDesktops();
        this.createTime=one.getCreateTime();
        this.occupationName=occupationName;
        this.roleName=roleName;
    }
}
