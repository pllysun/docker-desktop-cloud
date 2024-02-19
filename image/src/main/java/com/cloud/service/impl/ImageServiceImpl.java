package com.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.DTO.DeskTopDto;
import com.cloud.DTO.ImageDto;
import com.cloud.DTO.PageBean;
import com.cloud.entity.Image;
import com.cloud.entity.Image_Label;
import com.cloud.entity.Image_Recommended;
import com.cloud.entity.PodController;
import com.cloud.mapper.*;
import com.cloud.service.ImageService;
import com.cloud.utils.TypeUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {
    @Autowired
    ImageMapper imageMapper;

    @Autowired
    LabelMapper labelMapper;

    @Autowired
    Image_LabelMapper imageLabelMapper;

    @Autowired
    Image_RecommendedMapper imageRecommendedMapper;

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    ContainerMapper containerMapper;

    @Autowired
    PodControllerMapper podControllerMapper;
    @Override
    public List<ImageDto> getAllOfficial(Integer pageNum, Integer pageSize, String imageName, String imageSystem, Integer recommendedCpu, Integer recommendedMemory, Integer recommendedSystemDisk, Integer recommendedDataDisk,List<String> labelName) {
        PageHelper.startPage(pageNum, pageSize);
        Integer count=0;
        if(labelName!=null)
            count=labelName.size();//标签数量
        List<ImageDto> imageDtos= imageMapper.getAllOfficial(imageName,imageSystem,recommendedCpu,recommendedMemory,recommendedSystemDisk,recommendedDataDisk,labelName,count);
        for(ImageDto imageDto:imageDtos){
            List<String> list=labelMapper.getlabel(imageDto.getImageId());
            imageDto.setLabelName(list);
        }
        Page<ImageDto> page= (Page<ImageDto>) imageDtos;
        return page.getResult();
    }

    @Override
    public PageBean getAllUser(Integer pageNum, Integer pageSize, String imageName, String imageSystem, Integer recommendedCpu, Integer recommendedMemory, Integer recommendedSystemDisk, Integer recommendedDataDisk,List<String> labelName) {
        PageHelper.startPage(pageNum, pageSize);
        Integer count=0;
        if(labelName!=null)
            count=labelName.size();//标签数量
        List<ImageDto> imageDtos= imageMapper.getAllUser(imageName,imageSystem,recommendedCpu,recommendedMemory,recommendedSystemDisk,recommendedDataDisk,labelName,count);
        for(ImageDto imageDto:imageDtos){
            List<String> list=labelMapper.getlabel(imageDto.getImageId());
            imageDto.setLabelName(list);
        }
        Page<ImageDto> page= (Page<ImageDto>) imageDtos;
        PageBean pageBean=new PageBean(page.getTotal(),page.getResult());
        return pageBean;
    }

    @Override
    public void deleteImage(Integer id) {
        //删除镜像标签表
        LambdaQueryWrapper<Image_Label> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Image_Label::getImageId,id);
        imageLabelMapper.delete(queryWrapper);
        //删除镜像推荐配置表
        LambdaQueryWrapper<Image_Recommended> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(Image_Recommended::getImageId,id);
        imageRecommendedMapper.delete(queryWrapper1);
        //删除镜像表
        imageMapper.deleteById(id);
    }

    @Override
    public List<ImageDto> getRecommended(Integer userId) {
        Integer recommendedId=usersMapper.getRecommendedId(userId);
        return imageMapper.getRecommended(recommendedId);
   }

    @Override
    public void setDeskTop(Integer userId, String ip, DeskTopDto deskTopDto) {
        PodController podController=TypeUtil.CreatePodController(userId,deskTopDto,ip);
        podControllerMapper.insert(podController);
    }
}
