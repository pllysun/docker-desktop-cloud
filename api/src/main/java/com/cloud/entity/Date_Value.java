package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("date_value")
public class Date_Value {
    /**
     * 数据id
     */
    @TableId
    private Integer dateValueId;

    /**
     * 时间
     */
    private LocalDateTime dateTime;

    /**
     * 镜像使用次数
     */
    private Integer imageUseCount;

    /**
     * 容器存在数
     */
    private Integer deskCount;

    /**
     * 今天开机数
     */
    private Integer deskUse;
}
