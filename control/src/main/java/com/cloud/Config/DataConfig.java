package com.cloud.Config;

import com.cloud.DTO.DateDeskCount;
import com.cloud.DTO.DateImageUseCount;
import com.cloud.DTO.DateSumAndUseDto;
import com.cloud.entity.ConfigEntity;
import com.cloud.entity.Date_Value;
import com.cloud.mapper.DateValueMapper;
import com.cloud.mapper.ImageMapper;
import com.cloud.mapper.PodControllerMapper;
import com.cloud.utils.TypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataConfig {
    @Autowired
    private DateValueMapper dateValueMapper;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private PodControllerMapper podControllerMapper;

    /**
     * 每天获取数据
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void insertDateValue() {
        List<DateImageUseCount> imageUseCount=imageMapper.selectListCount();
        List<DateDeskCount> deskCount=podControllerMapper.getDeskTopCount();
        LocalDateTime nowTime=LocalDateTime.now();
        LocalDateTime startTime=nowTime.minusDays(ConfigEntity.Today);
        List<DateSumAndUseDto> dateSumAndUseDtos=podControllerMapper.getUseDeskTop(startTime,nowTime);
        List<Date_Value> list= TypeUtil.CreateDateValue(imageUseCount,deskCount,dateSumAndUseDtos);
        for (Date_Value dateValue:list)
            dateValueMapper.insert(dateValue);
    }



}
