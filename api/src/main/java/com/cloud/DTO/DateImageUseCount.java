package com.cloud.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DateImageUseCount {


    /**
     * 时间
     */
    private LocalDateTime dateTime;

    /**
     * 镜像使用次数
     */
    private Integer imageUseCount;

    /**
     * 用户id
     */
    private Integer userId;
}
