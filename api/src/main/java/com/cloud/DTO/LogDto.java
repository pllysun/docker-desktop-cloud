package com.cloud.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogDto {

    /**
     * 日志id
     */
    private Long logId;

    /**
     * 日志类型名称
     */
    private String logTypeName;

    /**
     *  日志发生时间
     */
    private LocalDateTime localTime;

    /**
     * 日志内容
     */
    private String logContent;
}
