package com.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.DTO.LogDto;
import com.cloud.entity.Log;
import com.cloud.mapper.LogMapper;
import com.cloud.service.LogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public List<LogDto> listAll(Integer pageNum, Integer pageSize,Integer userId, String logTypeName) {
        PageHelper.startPage(pageNum, pageSize);
        List<LogDto> logDtos = logMapper.listAll(userId, logTypeName);
        Page<LogDto> page=(Page<LogDto>) logDtos;
        return page.getResult();
    }

    @Override
    public List<LogDto> manageListAll(Integer pageNum, Integer pageSize, String logTypeName) {
        PageHelper.startPage(pageNum, pageSize);
        List<LogDto> logDtos = logMapper.listAll(null,logTypeName);
        Page<LogDto> page=(Page<LogDto>) logDtos;
        return page.getResult();
    }
}
