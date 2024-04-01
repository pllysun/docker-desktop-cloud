package com.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.entity.ConfigEntity;
import com.cloud.entity.Network;
import com.cloud.mapper.LogMapper;
import com.cloud.mapper.NetworkMapper;
import com.cloud.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class NetworkServiceImpl extends ServiceImpl<NetworkMapper, Network> implements NetworkService {
    @Autowired
    private  NetworkMapper networkMapper;

    @Autowired
    private LogMapper logMapper;

    @Override
    public void log(Integer userId,Integer type, String Content) {
        LocalDateTime dateTime=LocalDateTime.now();
        logMapper.insertLog(userId,type,dateTime,Content);
    }

    @Override
    public boolean networkExist(Network network) {
        Integer count=networkMapper.Exist(network.getPodSelector(),network.getUserId());
        return count!=0;//如果不等于0说明存在
    }


}
