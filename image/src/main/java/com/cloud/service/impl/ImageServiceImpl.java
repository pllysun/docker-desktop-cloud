package com.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.DTO.DeskTopDto;
import com.cloud.DTO.ImageDto;
import com.cloud.DTO.PageBean;
import com.cloud.entity.*;
import com.cloud.mapper.*;
import com.cloud.service.ImageService;
import com.cloud.utils.TypeUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {
    @Autowired
    ImageMapper imageMapper;

    @Autowired
    LabelMapper labelMapper;

    @Autowired
    Image_LabelMapper imageLabelMapper;


    @Autowired
    UsersMapper usersMapper;

    @Autowired
    ContainerMapper containerMapper;

    @Autowired
    PodControllerMapper podControllerMapper;


    @Autowired
    LogMapper logMapper;

    @Autowired
    RecommendedMapper recommendedMapper;

    @Autowired
    NetworkMapper networkMapper;


    @Override
    public List<ImageDto> getIndividualImage(Integer userId, String imageRemark, String imageSystem, Integer recommendedCpu, Integer recommendedMemory, Integer recommendedSystemDisk, Integer recommendedDataDisk,List<String> labelName) {
        int count=0;
        if(labelName!=null)
            count=labelName.size();//标签数量
        log.info("count:{}",count);
        List<ImageDto> imageDtos= imageMapper.getIndividualImage(userId,imageRemark,imageSystem,recommendedCpu,recommendedMemory,recommendedSystemDisk,recommendedDataDisk,labelName,count);
        for(ImageDto imageDto:imageDtos){
            List<String> list=labelMapper.getlabel(imageDto.getImageId(),imageDto.getRecommendedId());
            imageDto.setLabelName(list);
        }
        return  imageDtos;
    }

    @Override
    public List<ImageDto> getAllOfficial(Integer pageNum, Integer pageSize, String imageName,
                                         String imageSystem, Integer recommendedCpu, Integer recommendedMemory,
                                         Integer recommendedSystemDisk, Integer recommendedDataDisk,List<String> labelName) {
        PageHelper.startPage(pageNum, pageSize);
        int count=0;
        if(labelName!=null)
            count=labelName.size();//标签数量
        log.info("count:{}",count);
        List<ImageDto> imageDtos= imageMapper.getAllOfficial(imageName,imageSystem,recommendedCpu,recommendedMemory,recommendedSystemDisk,recommendedDataDisk,labelName,count);
        for(ImageDto imageDto:imageDtos){
            List<String> list=labelMapper.getlabel(imageDto.getImageId(),imageDto.getRecommendedId());
            imageDto.setLabelName(list);
        }
        Page<ImageDto> page= (Page<ImageDto>) imageDtos;
        return page.getResult();
    }

    @Override
    public PageBean getAllUser(Integer pageNum, Integer pageSize, String imageName, String imageSystem, Integer recommendedCpu, Integer recommendedMemory, Integer recommendedSystemDisk, Integer recommendedDataDisk,List<String> labelName) {
        PageHelper.startPage(pageNum, pageSize);
        int count=0;
        if(labelName!=null)
            count=labelName.size();//标签数量
        List<ImageDto> imageDtos= imageMapper.getAllUser(imageName,imageSystem,recommendedCpu,recommendedMemory,recommendedSystemDisk,recommendedDataDisk,labelName,count);
        for(ImageDto imageDto:imageDtos){
            List<String> list=labelMapper.getlabel(imageDto.getImageId(),imageDto.getRecommendedId());
            imageDto.setLabelName(list);
        }
        Page<ImageDto> page= (Page<ImageDto>) imageDtos;
        PageBean pageBean=new PageBean(page.getTotal(),page.getResult());
        return pageBean;
    }

    @Override
    @Transactional
    public void deleteImage(Integer userId,String imageId) {
        //todo userid用来写日志
        LocalDateTime  dateTime = LocalDateTime.now();
        logMapper.insertLog(userId,ConfigEntity.Delete_Image_Log_Type,dateTime,ConfigEntity.Delete_Image_Log_Content+imageId);
        //删除镜像标签表
        LambdaQueryWrapper<Image_Label> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Image_Label::getImageId,imageId);
        imageLabelMapper.delete(queryWrapper);
        //删除镜像表
        imageMapper.deleteById(imageId);
    }

    @Override
    public List<ImageDto> getRecommended(Integer userId) {
        Integer recommendedId=usersMapper.getRecommendedId(userId);
        List<ImageDto> imageDtos=imageMapper.getRecommended(recommendedId);
        for(ImageDto imageDto:imageDtos){
            List<String> list=labelMapper.getlabel(imageDto.getImageId(),imageDto.getRecommendedId());
            imageDto.setLabelName(list);
        }
        return  imageDtos;
   }


    @Override
    @Transactional
    public void setDeskTop(Integer userId, String ip, DeskTopDto deskTopDto) {
        PodController podController=TypeUtil.CreatePodController(userId,deskTopDto,ip);
        podControllerMapper.insert(podController);
        LocalDateTime dateTime=LocalDateTime.now();//当前时间
        logMapper.insertLog(userId,ConfigEntity.Image_Log_Type,dateTime,ConfigEntity.Image_Log_Content+podController.getPodControllerName());
    }

    @Override
    @Transactional
    public void makeImage(Integer userId,ImageDto imageDto) {
        //todo 创建镜像(利用初始镜像生成推荐镜像)
        LocalDateTime dateTime=LocalDateTime.now();//当前时间
        logMapper.insertLog(userId,ConfigEntity.Create_Image_Log_Type,dateTime,ConfigEntity.Create_Image_Log_Content+imageDto.getImageRemark());
        String imageName=imageMapper.selectImageName(imageDto.getImageSystem());
        String imageId= String.valueOf(UUID.randomUUID());
        imageMapper.insert(TypeUtil.CreateImage(imageDto,userId,imageName,imageId));
        //查看推荐配置中是否存在该配置-->存在则返回id,不存在则先创建再连接
        Integer recommendId = recommendedMapper.getRecommendedId(imageDto);
        //创建推荐,推荐配置不存在
        if(recommendId==null) {
            recommendedMapper.addRecommended(imageDto);
            recommendId=recommendedMapper.getRecommendedId(imageDto);
        }
        //建立连接
        //todo 推荐配置的id
        //解析标签，建立连接
        List<String> tags=imageDto.getLabelName();
        for(String tag:tags){
            Integer labelId=labelMapper.getLabelId(tag);
            imageLabelMapper.insert(new Image_Label(imageId,labelId,recommendId));
        }
    }

    @Override
    @Transactional
    public void updateImage(Integer userId,ImageDto imageDto) {
        //todo 先断开配置—镜像连接，查看更新的配置是否存在，存在则直接连接，不存在创建后进行连接
        LocalDateTime dateTime=LocalDateTime.now();//当前时间
        LambdaQueryWrapper<Image> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(Image::getImageId,imageDto.getImageId());
        Image oldImage=imageMapper.selectOne(queryWrapper1);
        log.info("imageDto{}",imageDto);
        logMapper.insertLog(userId,ConfigEntity.Update_Image_Log_Type,dateTime,ConfigEntity.Update_Image_Log_Content(oldImage.getImageRemark())+imageDto.getImageRemark());
        LambdaQueryWrapper<Image_Label> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Image_Label::getImageId,imageDto.getImageId()).eq(Image_Label::getRecommendedId,imageDto.getRecommendedId());
        imageLabelMapper.delete(queryWrapper);//这里的recommendedId是旧配置的id
        //进行镜像名称更新
        imageMapper.updateById(TypeUtil.CreateImage(imageDto,imageDto.getUserId(),imageDto.getImageName(),imageDto.getImageId()));
        Integer recommendId = recommendedMapper.getRecommendedId(imageDto);
        //创建推荐,推荐配置不存在
        if(recommendId==null) {
            recommendedMapper.addRecommended(imageDto);
            recommendId=recommendedMapper.getRecommendedId(imageDto);
        }
        //建立连接
        //todo 推荐配置的id
        //解析标签，建立连接
        List<String> tags=imageDto.getLabelName();
        for(String tag:tags){
            Integer labelId=labelMapper.getLabelId(tag);
            imageLabelMapper.insert(new Image_Label(imageDto.getImageId(), labelId,recommendId));
        }
    }

    @Override
    public boolean ImageExist(ImageDto imageDto) {
        //false 即为不存在，true即为存在
        LambdaQueryWrapper<Recommend> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Recommend::getRecommendedCpu,imageDto.getRecommendedCpu()).eq(Recommend::getRecommendedMemory,imageDto.getRecommendedMemory());
        Recommend recommend=recommendedMapper.selectOne(queryWrapper);//获取与imageDto配置相同的id
        log.info("recommendId:{}",recommend);
        if(recommend==null)return false;//当前配置不存在说明不可能重复
        LambdaQueryWrapper<Image> queryImage=new LambdaQueryWrapper<>();
        queryImage.eq(Image::getImageRemark,imageDto.getImageRemark()).eq(Image::getImageSystem,imageDto.getImageSystem());
        Image image=imageMapper.selectOne(queryImage);
        if(image==null)return false;//当前镜像名字没有重复或者说系统没有重复就说明不可能重复
        //当镜像一致，配置一致时判定为相同镜像
        LambdaQueryWrapper<Image_Label> queryWrapper1 =new LambdaQueryWrapper<>();
        queryWrapper1.eq(Image_Label::getImageId,imageDto.getImageId()).eq(Image_Label::getRecommendedId,recommend.getRecommendedId());
        return imageLabelMapper.exists(queryWrapper1);//如果imageId+recommendedId存在，则判定为相同镜像+相同配置
    }

    @Override
    public List<String> getSystem() {
        return imageMapper.getSystem();
    }


    @Override
    public Network getNetwork(String networkId) {
        return networkMapper.selectById(networkId);
    }

    @Override
    public void networkAddDeskTop(String networkId) {
        networkMapper.addDeskTop(networkId);
    }

    @Override
    public boolean userDeskTopCountLimit(Integer userId) {
        Integer deskTopCount=usersMapper.deskTopCountLimit(userId);
        if(deskTopCount<ConfigEntity.Number_Of_Desktop_Limit) return false;
        return true;
    }

    @Override
    public void userAddDeskTop(Integer userId) {
        usersMapper.addDeskTop(userId);
    }

    @Override
    public void imageAddUse(String imageId) {
        imageMapper.addUse(imageId);
    }

    @Override
    public Integer getEndPort() {
        return  imageMapper.getEndPort();
    }


}
