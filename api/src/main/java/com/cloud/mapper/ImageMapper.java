package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.DTO.DateImageUseCount;
import com.cloud.DTO.ImageDto;
import com.cloud.entity.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageMapper extends BaseMapper<Image> {

    List<ImageDto> getAllOfficial(@Param("imageName") String imageName,@Param("imageSystem") String imageSystem,@Param("recommendedCpu") Integer recommendedCpu,@Param("recommendedMemory") Integer recommendedMemory,@Param("recommendedSystemDisk") Integer recommendedSystemDisk,@Param("recommendedDataDisk") Integer recommendedDataDisk,@Param("labelName")List<String> labelName,@Param("count") Integer count);
//    List<ImageDto> getAllOfficial(@Param("imageName") String imageName,@Param("imageSystem") String imageSystem,@Param("recommendedCpu") Integer recommendedCpu,@Param("recommendedMemory") Integer recommendedMemory,@Param("recommendedSystemDisk") Integer recommendedSystemDisk,@Param("recommendedDataDisk") Integer recommendedDataDisk);
    List<ImageDto> getAllUser(@Param("imageName") String imageName,@Param("imageSystem") String imageSystem,@Param("recommendedCpu") Integer recommendedCpu,@Param("recommendedMemory") Integer recommendedMemory,@Param("recommendedSystemDisk") Integer recommendedSystemDisk,@Param("recommendedDataDisk") Integer recommendedDataDisk,@Param("labelName")List<String> labelName,@Param("count") Integer count);

    List<ImageDto> getRecommended(Integer recommendedId);

    String selectImageName(String imageSystem);

    List<String> getSystem();

    List<ImageDto> getIndividualImage(@Param("userId") Integer userId,@Param("imageName") String imageRemark,@Param("imageSystem") String imageSystem,@Param("recommendedCpu") Integer recommendedCpu,@Param("recommendedMemory") Integer recommendedMemory,@Param("recommendedSystemDisk") Integer recommendedSystemDisk,@Param("recommendedDataDisk") Integer recommendedDataDisk,@Param("labelName")List<String> labelName,@Param("count") Integer count);

    void addUse(String imageId);

    List<DateImageUseCount> selectListCount();

    Integer getEndPort();
}
