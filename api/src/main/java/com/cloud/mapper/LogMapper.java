package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.DTO.LogDto;
import com.cloud.entity.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LogMapper extends BaseMapper<Log> {
    List<LogDto> listAll(@Param("userId") Integer userId,@Param("logTypeName") String logTypeName);

    void insertLog(@Param("userId") Integer userId, @Param("logTypeId") Integer logTypeId, @Param("logTime")LocalDateTime logTime, @Param("logContent") String logContent);
}
