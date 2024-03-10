package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("network_table")
public class Network {
    /**
     * 网络id
     */
    @TableId
    private String networkId;

    /**
     *  网络名称
     */
    private String networkName;

    /**
     * 选择器(只包含value值)
     */
    private String podSelector;

    /**
     * 用户ip
     */
    private  String userIp;

    /**
     *  数量
     */
    private Integer podCount;

    /**
     * 网络创建时间
     */
    private LocalDateTime createTime;

    /**
     * 用户id(不显示)
     */
    private Integer userId;

}
