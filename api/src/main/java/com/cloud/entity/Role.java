package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "role_table")
public class Role {
    @TableId(value = "role_id")
    private Integer roleId;
    @TableField(value = "role_name")
    private String roleName;

}
