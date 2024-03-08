package com.cloud.controller;
//todo 这里要大改
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cloud.DTO.DeskTopDto;
import com.cloud.DTO.ImageDto;
import com.cloud.DTO.PageBean;
import com.cloud.config.LinuxConfig;
import com.cloud.entity.ConfigEntity;
import com.cloud.entity.Image;
import com.cloud.entity.Network;
import com.cloud.service.ImageService;
import com.cloud.service.K8sService;
import com.cloud.service.LinuxService;
import com.cloud.utils.R;
import com.cloud.utils.TypeUtil;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
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
//todo 修改增加镜像逻辑
@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private K8sService k8sService;

    @Autowired
    private LinuxService linuxService;

    @Autowired
    private Session session;
    /**
     * 测试接口
     * @return
     */
    @GetMapping("/hello")
    public R<Object> hello() {
        return R.success("hello image");
    }

    /**
     * 查看用户自己上传的镜像
     * @param labelName
     * @param userId
     * @param imageRemark
     * @param imageSystem
     * @param recommendedCpu
     * @param recommendedMemory
     * @param recommendedSystemDisk
     * @param recommendedDataDisk
     * @return
     */
    @GetMapping("/individual/{userId}")
    public R<Object> getIndividualImage(@RequestParam(defaultValue = "") List<String> labelName,@PathVariable Integer userId,String imageRemark, String imageSystem, Integer recommendedCpu, Integer recommendedMemory,
                                        Integer recommendedSystemDisk, Integer recommendedDataDisk){
        if(labelName.size()==0)labelName=null;
        List<ImageDto> imageDtos=imageService.getIndividualImage(userId,imageRemark,imageSystem,recommendedCpu,recommendedMemory,recommendedSystemDisk,recommendedDataDisk,labelName);
        return R.success(imageDtos);
    }

    /**
     * 查询官方镜像列表+推荐镜像
     * @param pageNum
     * @param pageSize
     * @param imageSystem
     * @param recommendedCpu
     * @param recommendedMemory
     * @param recommendedSystemDisk
      * @param recommendedDataDisk
     * @return
     */
    @GetMapping("/official/{userId}")
    public R<Object> listOfficial(@RequestParam(defaultValue = "") List<String> labelName,@PathVariable Integer userId
                                 ,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize,
                                  String imageRemark, String imageSystem, Integer recommendedCpu, Integer recommendedMemory,
                                  Integer recommendedSystemDisk, Integer recommendedDataDisk) {
        log.info("labelName:{}",labelName);
        if(labelName.size()==0)labelName=null;
        //todo 推荐镜像
        List<ImageDto> imageRecommended = imageService.getRecommended(userId);
        //todo 全部官方镜像
        List<ImageDto> imageDtoList = imageService.getAllOfficial(pageNum,pageSize,imageRemark,imageSystem,recommendedCpu,recommendedMemory,recommendedSystemDisk,recommendedDataDisk,labelName);
        //todo 合并镜像
        imageDtoList.addAll(imageRecommended);
        return R.success(imageDtoList);
    }

    /**
     * 查看社区镜像
     * @param labelName
     * @param pageNum
     * @param pageSize
     * @param imageRemark
     * @param imageSystem
     * @param recommendedCpu
     * @param recommendedMemory
     * @param recommendedSystemDisk
     * @param recommendedDataDisk
     * @return
     */
    @GetMapping("/user")
    public R<Object> listUser(@RequestParam(defaultValue = "") List<String> labelName,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize,
                                  String imageRemark, String imageSystem, Integer recommendedCpu, Integer recommendedMemory,
                                  Integer recommendedSystemDisk, Integer recommendedDataDisk) {
        if(labelName.size()==0)labelName=null;
        PageBean pageBean=imageService.getAllUser(pageNum,pageSize,imageRemark,imageSystem,recommendedCpu,recommendedMemory,recommendedSystemDisk,recommendedDataDisk,labelName);
        return R.success(pageBean);
    }

    /**
     * 自定义镜像(要改)
     */
    @GetMapping("/custom")
    public R<Object> listCustom(){
        List<String> imageSystem= imageService.getSystem();
        return R.success(imageSystem);
    }

    //todo 需要修改k8s代码
    /**
     * 镜像转换为容器-->这里非常重要,pvc,pv,service,deployment，
     * @param userId
     * @param deskTopDto
     * @return
     * @throws ApiException
     * @throws IOException
     */
    @PostMapping("/desktop/{userId}")
    public  R<Object> createContainer(@PathVariable Integer userId, @RequestBody DeskTopDto deskTopDto) throws ApiException, JSchException, IOException {
        log.info("用户id:{},桌面:{}",userId,deskTopDto);
        //todo 判断这个容器是否可以正常创建（自定义镜像判断配置）
        if(TypeUtil.CheckResourceConfig(deskTopDto.getImageDto().getRecommendedCpu(),deskTopDto.getImageDto().getRecommendedMemory()))return R.fail("cpu和内存配置错误，请重新配置");
        Integer GB= linuxService.computeStore(session);
        if(TypeUtil.CheckConfig(GB,deskTopDto.getImageDto().getRecommendedDataDisk(),deskTopDto.getImageDto().getRecommendedSystemDisk()))return R.fail("配置错误，请重新配置");
        //随机唯一标识码
        String podControllerName= TypeUtil.generateLetterOnlyUUID();
        deskTopDto.setPodControllerName(podControllerName);
        //创建容器
        //利用虚拟机创建文件夹，并且将配置放入nfs中，重启nfs服务(3个)
        linuxService.createNfsFile(userId,deskTopDto,session);
        linuxService.connectNfs(userId,deskTopDto,session);
        linuxService.restartNfs(session);
        Network network=imageService.getNetwork(deskTopDto.getNetworkId());
        String ip=k8sService.createDeskTop(userId,deskTopDto,network);
        //桌面容器添加到数据库中
        imageService.setDeskTop(userId,ip,deskTopDto);
        //该网络添加一个云桌面
        imageService.addDeskTop(deskTopDto.getNetworkId());
        return R.success("创建成功"+"ip:"+ip);
    }


    /**
     * 删除镜像
     * @param imageId-->镜像id
     * @return
     */
    @DeleteMapping("/{userId}")
    public R<Object> deleteImage(@PathVariable Integer userId,@RequestParam("imageId") String imageId){
        imageService.deleteImage(userId,imageId);
        return R.success("删除镜像成功");
    }


    /**
     * 增加镜像
     * @param imageDto 此处的dto没有id
     * @return
     */
    @PostMapping("/make/{userId}")
    public R<Object> makeImage(@PathVariable Integer userId,@RequestBody ImageDto imageDto) throws JSchException, IOException {
        //todo 判断这个容器是否可以正常创建（自定义镜像判断配置）
        Integer GB= linuxService.computeStore(session);
        if(TypeUtil.CheckConfig(GB,imageDto.getRecommendedDataDisk(),imageDto.getRecommendedSystemDisk()))return R.fail("配置错误，请重新配置");
        if(TypeUtil.CheckResourceConfig(imageDto.getRecommendedCpu(),imageDto.getRecommendedMemory()))return R.fail("cpu和内存配置错误，请重新配置");
        if(imageService.ImageExist(imageDto))return  R.fail("镜像已存在");
        //todo 标签为0时报错
        if(imageDto.getLabelName()==null)return R.fail("至少需要一个标签");
        imageService.makeImage(userId,imageDto);
        return R.success("制作新镜像成功");
    }

    /**
     * 修改镜像（也可以修改名称，介绍）
     * @param imageDto
     * @return
     */
    @PostMapping("/update/{userId}")
    public R<Object> updateImage(@PathVariable Integer userId,@RequestBody ImageDto imageDto) throws JSchException, IOException {
        ///todo 判断这个容器是否可以正常创建（自定义镜像判断配置）
        Integer GB= linuxService.computeStore(session);
        if(TypeUtil.CheckConfig(GB,imageDto.getRecommendedDataDisk(),imageDto.getRecommendedSystemDisk()))return R.fail("配置错误，请重新配置");
        if(TypeUtil.CheckResourceConfig(imageDto.getRecommendedCpu(),imageDto.getRecommendedMemory()))return R.fail("cpu和内存配置错误，请重新配置");
        if(imageService.ImageExist(imageDto))return  R.fail("镜像已存在");
        //todo 标签为0时报错
        if(imageDto.getLabelName()==null)return R.fail("至少需要一个标签");
        imageService.updateImage(userId,imageDto);
        return R.success("修改镜像成功");
    }



}
