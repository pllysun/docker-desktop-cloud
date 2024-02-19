package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.DTO.ContainerDto;
import com.cloud.entity.PodController;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContainerMapper extends BaseMapper<PodController> {
    List<ContainerDto> List(Integer userId);

    void closeContainerState(ContainerDto containerDto);

    void openContainerState(ContainerDto containerDto);

    void updateContainerName(@Param("podControllerId") Integer podControllerId,@Param("containerName") String containerName);
}
