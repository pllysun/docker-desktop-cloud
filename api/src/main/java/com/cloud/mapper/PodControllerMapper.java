package com.cloud.mapper;

import com.cloud.entity.PodController;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PodControllerMapper {


    public void insert(PodController podController);
}
