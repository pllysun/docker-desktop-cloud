package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("log_table")
public class Log {


    @TableId
    private Long logId;

    /**
     * 当前用户日志（管理员无视这个东西）
     */
    private Long userId;

    /**
     * 日志类型
     */
    private Long logTypeId;

    /**
     * 日志创建时间
     */
    private LocalDateTime logTime;

    /**
     * 日志内容
     */
    private String logContent;
}
