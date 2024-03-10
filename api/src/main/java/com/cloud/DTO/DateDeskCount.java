package com.cloud.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DateDeskCount {
    /**
     * 时间
     */
    private LocalDateTime dateTime;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 使用数量
     */
    private Integer deskCount;
}
