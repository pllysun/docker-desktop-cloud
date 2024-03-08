package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.entity.label;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LabelMapper extends BaseMapper<label>{


    List<String> getlabel(@Param("imageId") String imageId,@Param("recommendedId") Integer recommendedId);

    Integer getLabelId(String labelName);
}
