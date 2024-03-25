package com.cloud.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 桌面容器--->容器DTO
 */
@Data
public class ContainerDto {
    /**
     * 桌面id
     */
    private Integer podControllerId;

    /**
     * 桌面唯一名称
     */
    private String podControllerName;

    /**
     * 桌面名称
     */
    private String containerName;
    /**
     * 桌面所属网络id
     */
    private String networkId;

    /**
     * 桌面所属网络
     */
    private String networkName;

    /**
     * 使用用户id
     */
    private Integer userId;

    /**
     * 桌面ip地址
     */
    private String ipAddress;

    /**
     * 桌面状态
     */
    private String containerState;

    /**
     * 桌面版本
     */
    private String podControllerVersion;

    /**
     * cpu
     */
    private Integer podControllerCpu;

    /**
     * 内存
     */
    private Integer podControllerMemory;


    /**
     *  数据盘
     */
    private Integer podControllerDataDisk;

    /**
     *  创建时间
     */
    private LocalDateTime createTime;

    /**
     *  更新时间
     */
    private LocalDateTime updateTime;
}
