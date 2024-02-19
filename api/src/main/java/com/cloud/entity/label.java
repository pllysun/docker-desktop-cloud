package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("label_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class label {
    /**
     * 标签id
     */
    @TableId
    private Integer labelId;

    /**
     *  标签名
     */
    private  String labelName;
}
