package com.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.DTO.ContainerDto;
import com.cloud.entity.PodController;
import com.cloud.mapper.ContainerMapper;
import com.cloud.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContainerServiceImpl extends ServiceImpl<ContainerMapper, PodController> implements ContainerService {

    @Autowired
    ContainerMapper containerMapper;


    /**
     * 查询容器列表
     * @param userId
     * @return
     */
    @Override
    public List<ContainerDto> List(Integer userId) {
        return containerMapper.List(userId);
    }

    @Override
    public void closeContainerState(ContainerDto containerDto) {
        containerMapper.closeContainerState(containerDto);
    }

    @Override
    public void openContainerState(ContainerDto containerDto) {
        containerDto.setUpdateTime(LocalDateTime.now());
        containerMapper.openContainerState(containerDto);
    }

    @Override
    public void deleteByPodControllerName(ContainerDto containerDto) {
        containerMapper.deleteById(containerDto.getPodControllerId());
    }

    @Override
    public void updateContainerName(Integer podControllerId, String containerName) {
        containerMapper.updateContainerName(podControllerId, containerName);
    }
}
