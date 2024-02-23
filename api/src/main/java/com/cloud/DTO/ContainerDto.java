package com.cloud.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 桌面容器--->容器DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContainerDto {
    /**
     * 桌面id
     */
    private String podControllerId;
    /**
     * 桌面名称
     */
    private String podControllerName;
    /**
     * 网络id
     */
    private String networkId;
    /**
     * 桌面所属网络
     */
    private String networkName;
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
     *  系统盘
     */
    private Integer podControllerSystemDisk;

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