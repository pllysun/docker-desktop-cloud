package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName users
 */
@TableName(value ="users")
@Data
public class Users implements Serializable {
    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 昵称
     */
    private String nick;

    /**
     * 头像(地址)
     */
    private String icon;

    /**
     * 个人简介
     */
    private String biography;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 系统角色(与用户在社群中的角色区分) - 0:系统用户 -1:系统管理员
     */
    private Integer sysRole;

    /**
     * 删除标记
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}