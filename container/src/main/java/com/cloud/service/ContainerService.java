package com.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.DTO.ContainerDto;
import com.cloud.DTO.PageBean;
import com.cloud.DTO.UserImageDto;
import com.cloud.entity.PodController;
import com.cloud.entity.label;

import java.awt.*;
import java.util.List;

public interface ContainerService extends IService<PodController> {

    /**
     *  获取桌面容器列表
     * @param userId
     * @return
     */
    List<ContainerDto> List(Integer userId);


    void closeContainerState(Integer userId,ContainerDto containerDto);

    void openContainerState(Integer userId,ContainerDto containerDto);

    void deleteByPodControllerName(Integer userId,ContainerDto containerDto);

    void updateContainerName(Integer userId,Integer podControllerId, String containerName);

    void updateDataDisk(Integer userId,Integer podControllerId,Integer podControllerDataDisk);

    void reinstall(Integer userId, ContainerDto containerDto);


    PageBean adminList(Integer pageNum, Integer pageSize);

    List<ContainerDto> getTimeOutContainers();

    void deleteBySystem(ContainerDto container);

    void upload(UserImageDto userImageDto);

    void networkDeleteDeskTop(ContainerDto containerDto);

    void userDeleteDeskTop(Integer userId);

    List<label> getLabel();
}
