package com.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.DTO.LogDto;
import com.cloud.entity.Log;

import java.util.List;

public interface LogService extends IService<Log> {


    List<LogDto> listAll(Integer pageNum, Integer pageSize,Integer userId, String logTypeName);

    List<LogDto> manageListAll(Integer pageNum, Integer pageSize, String logTypeName);
}
