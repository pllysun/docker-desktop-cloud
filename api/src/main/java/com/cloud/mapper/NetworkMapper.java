package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.entity.Network;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NetworkMapper extends BaseMapper<Network> {

    void addDeskTop(String networkId);

    void deleteDeskTop(Integer networkId);
}
