package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("pod_controller_table")
public class PodController {

    /**
     * pod控制器id
     */
    @TableId
    private Long podControllerId;

    /**
     * 容器名称
     */
    private String containerName;

    /**
     * 容器状态
     */
    private String containerState;

    /**
     *  镜像id
     */
    private String imageId;

    /**
     * 拥有该桌面的用户id
     */
    private Integer userId;

    /**
     *  网络id
     */
    private String networkId;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 桌面名称
     */
    private String podControllerName;

    /**
     * 桌面cpu
     */
    private Integer podControllerCpu;

    /**
     *  桌面内存
     */
    private Integer podControllerMemory;

    /**
     *  桌面系统盘
     */
    private Integer podControllerSystemDisk;

    /**
     *   桌面数据盘
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

    /**
     *   桌面版本
     */
    private String podControllerVersion;
}
