package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "personalise_table")
public class Personalise {
    @TableId(value = "personalise_id")
    private Integer personaliseId;
    @TableField(value = "personalise_name")
    private String  personaliseName;
    @TableField(value = "personalise_type")
    private String  personaliseType;
    @TableField(value = "occupation_id")
    private String  occupationId;

}
