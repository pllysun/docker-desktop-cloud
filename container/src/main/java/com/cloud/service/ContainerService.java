package com.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.DTO.ContainerDto;
import com.cloud.entity.PodController;

import java.util.List;

public interface ContainerService extends IService<PodController> {

    /**
     *  获取桌面容器列表
     * @param userId
     * @return
     */
    List<ContainerDto> List(Integer userId);

    /**
     * 容器状态关机
     * @param containerDto
     */
    void closeContainerState(ContainerDto containerDto);

    /**
     *  容器状态开机
     * @param containerDto
     */
    void openContainerState(ContainerDto containerDto);

    void deleteByPodControllerName(ContainerDto containerDto);

    void updateContainerName(Integer podControllerId, String containerName);
}
