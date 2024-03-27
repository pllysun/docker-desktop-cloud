package com.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.DTO.DeskTopDto;
import com.cloud.DTO.ImageDto;
import com.cloud.DTO.PageBean;
import com.cloud.entity.Image;
import com.cloud.entity.Network;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public interface ImageService extends IService<Image> {

     List<ImageDto> getIndividualImage(Integer userId, String imageRemark, String imageSystem, Integer recommendedCpu, Integer recommendedMemory, Integer recommendedSystemDisk, Integer recommendedDataDisk,List<String> labelName) ;


    List<ImageDto> getAllOfficial(Integer pageNum, Integer pageSize, String imageName, String imageSystem, Integer recommendedCpu, Integer recommendedMemory, Integer recommendedSystemDisk, Integer recommendedDataDisk, List<String> labelName);

    PageBean getAllUser(Integer pageNum, Integer pageSize, String imageName, String imageSystem, Integer recommendedCpu, Integer recommendedMemory, Integer recommendedSystemDisk, Integer recommendedDataDisk,List<String> labelName);

    void deleteImage(Integer userId,String imageId);

    List<ImageDto> getRecommended(Integer userId);

    void setDeskTop(Integer userId, String ip, DeskTopDto deskTopDto);

    void makeImage(Integer userId,ImageDto imageDto);

    void updateImage(Integer userId,ImageDto imageDto);

    boolean ImageExist(ImageDto imageDto);

    List<String> getSystem();

    Network getNetwork(String networkId);

    void networkAddDeskTop(String networkId);

    boolean userDeskTopCountLimit(Integer userId);

    void userAddDeskTop(Integer userId);

    void imageAddUse(String imageId);

    Integer getEndPort();

    void customImage(Integer userId, ImageDto imageDto);
}
