package com.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.DTO.DeskTopDto;
import com.cloud.DTO.ImageDto;
import com.cloud.DTO.PageBean;
import com.cloud.entity.Image;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public interface ImageService extends IService<Image> {

    List<ImageDto> getAllOfficial(Integer pageNum, Integer pageSize, String imageName, String imageSystem, Integer recommendedCpu, Integer recommendedMemory, Integer recommendedSystemDisk, Integer recommendedDataDisk,List<String> labelName);

    PageBean getAllUser(Integer pageNum, Integer pageSize, String imageName, String imageSystem, Integer recommendedCpu, Integer recommendedMemory, Integer recommendedSystemDisk, Integer recommendedDataDisk,List<String> labelName);

    void deleteImage(Integer id);

    List<ImageDto> getRecommended(Integer userId);

    void setDeskTop(Integer userId, String ip, DeskTopDto deskTopDto);

}
