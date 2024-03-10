package com.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cloud.DTO.DateImageUseCount;
import com.cloud.DTO.DateSumAndUseDto;
import com.cloud.DTO.DeskTopControlDto;
import com.cloud.entity.ConfigEntity;
import com.cloud.entity.Date_Value;
import com.cloud.mapper.DateValueMapper;
import com.cloud.mapper.PodControllerMapper;
import com.cloud.service.ControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ControlServiceImpl implements ControlService {

    @Autowired
    private PodControllerMapper podControllerMapper;

    @Autowired
    private DateValueMapper dateValueMapper;

    /**
     * 获取使用中和关机中
     * @param userId
     * @return
     */
    @Override
    public DeskTopControlDto getDeskTopControl(Integer userId) {
        DeskTopControlDto controlDto = new DeskTopControlDto();
        controlDto.setUseDeskTopCount(podControllerMapper.getDeskTopControl(userId, ConfigEntity.Open_Container_Status));
        controlDto.setDownDeskTopCount(podControllerMapper.getDeskTopControl(userId,ConfigEntity.Close_Container_Status));
        return controlDto;
    }

    /**
     * 获取用户镜像使用次数
     * @param userId
     * @return
     */
    @Override
    public List<DateImageUseCount> getImageUseCount(Integer userId) {
        LocalDateTime localDateTime=LocalDateTime.now();
        LocalDateTime  startTime=localDateTime.minusDays(ConfigEntity.Date_Image_Use_Count);
        return dateValueMapper.getImageUseCount(userId,localDateTime,startTime);
    }

    /**
     * 7天过期比例
     * @param userId
     * @return
     */
    @Override
    public Integer getExpireProportion(Integer userId) {
        LocalDateTime nowTime=LocalDateTime.now();
        LocalDateTime deleteTime=nowTime.minusDays(ConfigEntity.Delete_Time);
        Integer nowCount=dateValueMapper.getCount(userId,nowTime);
        Integer sumCount=dateValueMapper.getCount(userId,deleteTime);
        return (nowCount/sumCount)*100;
    }

    @Override
    public List<DateSumAndUseDto> getSumAndUse(Integer userId) {
        LocalDateTime nowTime=LocalDateTime.now();
        LocalDateTime useTime=nowTime.minusDays(ConfigEntity.Use_Time);
        return dateValueMapper.getSumAndUse(userId,nowTime,useTime);
    }


}