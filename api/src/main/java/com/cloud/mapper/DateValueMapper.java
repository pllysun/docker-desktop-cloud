package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.DTO.DateImageUseCount;
import com.cloud.DTO.DateSumAndUseDto;
import com.cloud.entity.Date_Value;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface DateValueMapper extends BaseMapper<Date_Value> {


    List<DateImageUseCount> getImageUseCount(@Param("userId") Integer userId,@Param("startTime") LocalDateTime startTime,@Param("nowTime") LocalDateTime nowTime);



    Integer getCount(@Param("userId") Integer userId,@Param("dateTime") LocalDateTime dateTime,@Param("yesterday") LocalDateTime yesterday);

    List<DateSumAndUseDto> getSumAndUse(@Param("userId") Integer userId,@Param("nowTime") LocalDateTime nowTime,@Param("useTime") LocalDateTime useTime);
}
