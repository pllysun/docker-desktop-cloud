package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName users
 */
@TableName(value ="user_table")
@Data
public class Users implements Serializable {
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
     * 密码
     */
    private String password;

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
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private String age;

    /**
     *  职业id
     */
    private Integer occupationId;

    /**
     * 系统角色(与用户在社群中的角色区分) - 0:系统用户 -1:系统管理员
     */
    private Integer roleId;

    /**
     *  桌面数量
     */
    private Integer NumberOfDesktops;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }
}