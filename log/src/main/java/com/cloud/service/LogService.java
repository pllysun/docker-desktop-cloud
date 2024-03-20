package com.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.DTO.LogDto;
import com.cloud.DTO.PageBean;
import com.cloud.entity.Log;

import java.util.List;

public interface LogService extends IService<Log> {


    PageBean listAll(Integer pageNum, Integer pageSize, Integer userId, String logTypeName);

    PageBean manageListAll(Integer pageNum, Integer pageSize, String logTypeName);
}
