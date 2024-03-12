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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
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
        log.info("开始时间：{},结束时间:{}",startTime,localDateTime);
        return dateValueMapper.getImageUseCount(userId,startTime,localDateTime);
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
        log.info("开始时间：{},结束时间:{}",deleteTime,nowTime);
        Integer nowCount=dateValueMapper.getCount(userId,nowTime,nowTime.minusDays(1));//现在的桌面数
        log.info("现在的桌面数：{}",nowCount);
        Integer sumCount=dateValueMapper.getCount(userId,deleteTime,deleteTime.minusDays(1));//7天前的桌面数
        log.info("7天前的桌面数：{}",sumCount);
        if(nowCount==null)return 0;
        return (nowCount*100/sumCount*100)/100;
    }

    /**
     * 总数和桌面数
     * @param userId
     * @return
     */
    @Override
    public List<DateSumAndUseDto> getSumAndUse(Integer userId) {
        LocalDateTime nowTime=LocalDateTime.now();
        LocalDateTime useTime=nowTime.minusDays(ConfigEntity.Use_Time);
        return dateValueMapper.getSumAndUse(userId,nowTime,useTime);
    }


}