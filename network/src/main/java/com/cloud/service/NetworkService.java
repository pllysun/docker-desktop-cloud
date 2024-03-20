package com.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.entity.Network;

public interface NetworkService extends IService<Network> {

    void log(Integer userId,Integer type, String Content);

    boolean networkExist(Network network);
}
