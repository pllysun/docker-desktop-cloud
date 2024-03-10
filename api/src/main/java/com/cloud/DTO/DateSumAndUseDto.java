package com.cloud.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DateSumAndUseDto {
    /**
     * 时间
     */
    private LocalDateTime dateTime;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     *  总和
     */
    private Integer sumNum;
    /**
     * 使用数
     */
    private  Integer useNum;

}
