package com.cloud.mapper;

import com.cloud.DTO.DateDeskCount;
import com.cloud.DTO.DateSumAndUseDto;
import com.cloud.entity.PodController;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PodControllerMapper {


    void insert(PodController podController);

    Integer getDeskTopControl(@Param("userId") Integer userId,@Param("containerState")String containerState);

    List<DateDeskCount> getDeskTopCount();

    List<DateSumAndUseDto> getUseDeskTop(@Param("startTime")LocalDateTime startTime,@Param("nowTime")LocalDateTime nowTime);
}
