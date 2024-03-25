package com.cloud.service.impl;

import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.DTO.ContainerDto;
import com.cloud.DTO.PageBean;
import com.cloud.DTO.UserImageDto;
import com.cloud.entity.*;
import com.cloud.entity.Image;
import com.cloud.mapper.*;
import com.cloud.service.ContainerService;
import com.cloud.utils.TypeUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ContainerServiceImpl extends ServiceImpl<ContainerMapper, PodController> implements ContainerService {

    @Autowired
    ContainerMapper containerMapper;

    @Autowired
    LogMapper logMapper;

    @Autowired
    ImageMapper imageMapper;

    @Autowired
    LabelMapper labelMapper;

    @Autowired
    RecommendedMapper recommendedMapper;

    @Autowired
    Image_LabelMapper imageLabelMapper;

    @Autowired
    NetworkMapper networkMapper;

    @Autowired
    UsersMapper usersMapper;

    @Override
    public List<ContainerDto> List(Integer userId) {
        return containerMapper.List(userId);
    }

    @Override
    @Transactional
    public void closeContainerState(Integer userId,ContainerDto containerDto) {
        LocalDateTime dateTime=LocalDateTime.now();
        logMapper.insertLog(userId, ConfigEntity.Close_Log_Type,dateTime,ConfigEntity.Close_Log_Content+containerDto.getContainerName());
        log.info("关闭容器内容：{}",ConfigEntity.Close_Log_Content+containerDto.getPodControllerName());
        containerMapper.closeContainerState(containerDto);
    }

    @Override
    @Transactional
    public void openContainerState(Integer userId,ContainerDto containerDto) {
        LocalDateTime dateTime=LocalDateTime.now();
        containerDto.setUpdateTime(dateTime);//修改上次时间
        logMapper.insertLog(userId, ConfigEntity.Open_Log_Type,dateTime,ConfigEntity.Open_Log_Content+containerDto.getContainerName());
        log.info("打开容器：{}",ConfigEntity.Open_Log_Content+containerDto.getPodControllerName());
        containerMapper.openContainerState(containerDto);
    }

    @Override
    @Transactional
    public void deleteByPodControllerName(Integer userId,ContainerDto containerDto) {
        LocalDateTime dateTime=LocalDateTime.now();
        logMapper.insertLog(userId, ConfigEntity.Delete_Log_Type,dateTime,ConfigEntity.Delete_Log_Content+containerDto.getContainerName());
        log.info("删除容器内容：{}",ConfigEntity.Delete_Log_Content+containerDto.getPodControllerName());
        containerMapper.deleteById(containerDto.getPodControllerId());
    }

    @Override
    @Transactional
    public void updateContainerName(Integer userId,Integer podControllerId, String containerName) {
        LocalDateTime dateTime=LocalDateTime.now();
        String OldContainerName= containerMapper.selectContainerNameById(podControllerId);
        String PodControllerName= containerMapper.selectPodControllerNameById(podControllerId);
        logMapper.insertLog(userId, ConfigEntity.Update_Log_Type,dateTime,OldContainerName+ConfigEntity.Update_Log_Content(PodControllerName)+containerName);
        log.info("修改容器内容：{}",OldContainerName+ConfigEntity.Update_Log_Content(PodControllerName)+containerName);
        containerMapper.updateContainerName(podControllerId, containerName);
    }

    @Override
    @Transactional
    public void updateDataDisk(Integer userId,Integer podControllerId,Integer podControllerDataDisk) {
        LocalDateTime dateTime=LocalDateTime.now();
        String PodControllerName= containerMapper.selectPodControllerNameById(podControllerId);
        logMapper.insertLog(userId, ConfigEntity.Expansion_Log_Type,dateTime,ConfigEntity.Expansion_Log_Content(PodControllerName,podControllerDataDisk));
        log.info("扩容系统盘：{}",ConfigEntity.Expansion_Log_Content(PodControllerName,podControllerDataDisk));
        containerMapper.updateDataDisk(podControllerId, podControllerDataDisk);
    }

    @Override
    public void reinstall(Integer userId, ContainerDto containerDto) {
        LocalDateTime dateTime=LocalDateTime.now();
        logMapper.insertLog(userId, ConfigEntity.Reinstall_Log_Type,dateTime,ConfigEntity.Reinstall_Log_Content+containerDto.getContainerName());
        log.info("重新安装：{}",ConfigEntity.Reinstall_Log_Content+containerDto.getPodControllerName());
    }

    @Override
    public PageBean adminList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ContainerDto> containerDtoList=containerMapper.List(null);
        Page<ContainerDto> page=(Page<ContainerDto>) containerDtoList;
        //plan1-->无总数
        //return page.getResult();
        //plan2-->有总数
        PageBean pageBean=new PageBean(page.getTotal(),page.getResult());
        return pageBean;
    }

    @Override
    public List<ContainerDto> getTimeOutContainers() {
        return containerMapper.getTimeOutContainers(ConfigEntity.Delete_Time);
    }

    @Override
    public void deleteBySystem(ContainerDto container) {
        containerMapper.deleteById(container.getPodControllerId());
    }

    @Override
    public void upload(UserImageDto userImageDto) {
        //相当于增加镜像，将container,label分解image类型，recommend,image_label
        Image image= TypeUtil.CreateImage(userImageDto);
        imageMapper.insert(image);
        LambdaQueryWrapper<Recommend> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Recommend::getRecommendedCpu,userImageDto.getContainerDto().getPodControllerCpu())
                .eq(Recommend::getRecommendedMemory,userImageDto.getContainerDto().getPodControllerMemory())
                .eq(Recommend::getRecommendedDataDisk,userImageDto.getContainerDto().getPodControllerDataDisk());
        Recommend recommended= recommendedMapper.selectOne(queryWrapper);
        //连接配置和标签
        for(String label:userImageDto.getLabelName()){
            Integer labelId=labelMapper.getLabelId(label);
            imageLabelMapper.insert(new Image_Label(image.getImageId(), recommended.getRecommendedId(),labelId));
        }
    }

    @Override
    public void networkDeleteDeskTop(ContainerDto containerDto) {
        networkMapper.deleteDeskTop(containerDto.getNetworkId());
    }

    @Override
    public void userDeleteDeskTop(Integer userId) {
        usersMapper.deleteDeskTop(userId);
    }

    @Override
    public List<String> getLabel() {
        return labelMapper.selectNameList();
    }
}