package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.DTO.ImageDto;
import com.cloud.entity.Recommend;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecommendedMapper extends BaseMapper<Recommend> {

    Integer getRecommendedId(ImageDto imageDto);

    void addRecommended(ImageDto imageDto);
}
