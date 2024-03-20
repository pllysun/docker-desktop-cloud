package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.entity.Network;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NetworkMapper extends BaseMapper<Network> {

    void addDeskTop(String networkId);

    void deleteDeskTop(String networkId);

    Integer Exist(@Param("networkName") String networkName,@Param("userId") Integer userId);
}
