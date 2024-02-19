package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("recommended_table")
public class Recommend {
    /**
     * 推荐id
     */
    @TableId
    private Integer recommendedId;

    /**
     * cpu
     */
    private Integer recommendedCpu;

    /**
     * 内存
     */
    private Integer recommendedMemory;

    /**
     *  系统盘
     */
    private Integer recommendedSystemDisk;

    /**
     *  数据盘
     */
    private Integer recommendedDataDisk;

}
