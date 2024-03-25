package com.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.DTO.LogDto;
import com.cloud.DTO.PageBean;
import com.cloud.entity.Log;
import com.cloud.mapper.LogMapper;
import com.cloud.service.LogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//todo 修改分页总数
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public PageBean listAll(Integer pageNum, Integer pageSize,Integer userId, String logTypeName) {
        PageHelper.startPage(pageNum, pageSize);
        List<LogDto> logDtos = logMapper.listAll(userId, logTypeName);
        Page<LogDto> page=(Page<LogDto>) logDtos;
        return new PageBean(page.getTotal(),page.getResult());
    }

    @Override
    public PageBean manageListAll(Integer pageNum, Integer pageSize, String logTypeName) {
        PageHelper.startPage(pageNum, pageSize);
        List<LogDto> logDtos = logMapper.listAll(null,logTypeName);
        Page<LogDto> page=(Page<LogDto>) logDtos;
        return new PageBean(page.getTotal(),page.getResult());
    }
}
