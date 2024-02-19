package com.cloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cloud.DTO.DeskTopDto;
import com.cloud.DTO.ImageDto;
import com.cloud.DTO.PageBean;
import com.cloud.entity.ConfigEntity;
import com.cloud.entity.Image;
import com.cloud.service.ImageService;
import com.cloud.service.K8sService;
import com.cloud.utils.R;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private K8sService k8sService;


    /**
     * 测试接口
     * @return
     */
    @GetMapping("/hello")
    public R<Object> hello() {
        return R.success("hello image");
    }

    /**
     * 查询官方镜像列表+推荐镜像
     * @param pageNum
     * @param pageSize
     * @param imageName
     * @param imageSystem
     * @param recommendedCpu
     * @param recommendedMemory
     * @param recommendedSystemDisk
     * @param recommendedDataDisk
     * @return
     */
    @GetMapping("/official/{userId}")
    public R<Object> listOfficial(@RequestParam(defaultValue = "") List<String> labelName,@PathVariable Integer userId,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize,
                                  String imageName, String imageSystem, Integer recommendedCpu, Integer recommendedMemory,
                                  Integer recommendedSystemDisk, Integer recommendedDataDisk) {
        log.info("labelName:{}",labelName);
        if(labelName.size()==0)labelName=null;
        //todo 推荐镜像
        List<ImageDto> imageRecommended = imageService.getRecommended(userId);
        //todo 全部官方镜像
        List<ImageDto> imageDtoList = imageService.getAllOfficial(pageNum,pageSize,imageName,imageSystem,recommendedCpu,recommendedMemory,recommendedSystemDisk,recommendedDataDisk,labelName);
        //todo 合并镜像
        imageDtoList.addAll(imageRecommended);
        return R.success(imageDtoList);
    }

    /**
     * 查询用户镜像列表-->分页且带总数
     */
    @GetMapping("/user")
    public R<Object> listUser(@RequestParam(defaultValue = "") List<String> labelName,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize,
                                  String imageName, String imageSystem, Integer recommendedCpu, Integer recommendedMemory,
                                  Integer recommendedSystemDisk, Integer recommendedDataDisk) {
        if(labelName.size()==0)labelName=null;
        PageBean pageBean=imageService.getAllUser(pageNum,pageSize,imageName,imageSystem,recommendedCpu,recommendedMemory,recommendedSystemDisk,recommendedDataDisk,labelName);
        return R.success(pageBean);
    }

    /**
     * 自定义镜像
     */
    @GetMapping("/custom")
    public R<Object> listCustom(){
        LambdaQueryWrapper<Image> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Image::getSource,0);
        List<Image> imageList=imageService.list(queryWrapper);
        return R.success(imageList);
    }

    /**
     * 镜像转换为容器-->这里非常重要,pvc,pv,service,deployment
     * @param userId
     * @param deskTopDto
     * @return
     * @throws ApiException
     * @throws IOException
     */
    @PostMapping("/desktop/{userId}")
    public  R<Object> createContainer(@PathVariable Integer userId, @RequestBody DeskTopDto deskTopDto) throws ApiException, IOException {
        log.info("用户id:{},桌面:{}",userId,deskTopDto);
        //todo 创建容器
        String ip=k8sService.createDeskTop(userId,deskTopDto);
        //todo 桌面容器添加到数据库中()
        imageService.setDeskTop(userId,ip,deskTopDto);
        return R.success("创建成功"+"ip:"+ip);
    }


    /**
     *  删除镜像-->管理员
     */
    @DeleteMapping("/{id}")
    public R<Object> deleteImage(@PathVariable Integer id){
        imageService.deleteImage(id);
        return R.success("删除成功");
    }
}
