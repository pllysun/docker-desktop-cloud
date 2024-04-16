package com.cloud.controller;
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


@RestController
@Slf4j
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
        imageRecommended.addAll(imageDtoList);
        return R.success(imageRecommended);
    }

    /**
     * 查看社区镜像(这里懒的改了，前端自己别写)
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
    public R<Object> listUser(@RequestParam(defaultValue = "") List<String> labelName,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "1000") Integer pageSize,
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

    /**
     * 镜像转换为桌面-->这里非常重要,pvc,pv,service,deployment，
     * @param userId
     * @param deskTopDto
     * @return
     * @throws ApiException
     * @throws IOException
     */
    @PostMapping("/desktop/{userId}")
    public  R<Object> createContainer(@PathVariable Integer userId, @RequestBody DeskTopDto deskTopDto) throws ApiException, JSchException, IOException {
        //todo 限制用户使用的桌面数量
        if(imageService.userDeskTopCountLimit(userId))
            return R.fail("用户可使用桌面数已达上限");
        log.info("用户id:{},桌面:{}",userId,deskTopDto);
        //todo 判断这个容器是否可以正常创建（自定义镜像判断配置）
        if(!TypeUtil.CheckResourceConfig(deskTopDto.getImageDto().getRecommendedCpu(),deskTopDto.getImageDto().getRecommendedMemory()))
            return R.fail("cpu和内存配置错误，请重新配置");
        //Integer GB= linuxService.computeStore(session);//查看剩余资源
        if(!TypeUtil.CheckConfig(deskTopDto.getImageDto().getRecommendedDataDisk()))
            return R.fail("配置错误，请重新配置");
        //随机唯一标识码
        String podControllerName= TypeUtil.generateLetterOnlyUUID();
        deskTopDto.setPodControllerName(podControllerName);
        //创建容器
        //利用虚拟机创建文件夹，并且将配置放入nfs中，重启nfs服务(3个)
        linuxService.createNfsFile(userId,deskTopDto,session);
        linuxService.connectNfs(userId,deskTopDto,session);
        linuxService.restartNfs(session);
        Network network=imageService.getNetwork(deskTopDto.getNetworkId());
        //获取podPort
        int podPort=2000;//port存在的话继续随机
        Integer endPort=imageService.getEndPort();
        if(endPort!=null)podPort=endPort+1;//当没有一个容器的时候使用2000，如果有容器获得到最后一位
        deskTopDto.setPodPort(podPort);
        String ip="";
        //创建容器
        if("ubuntu".equals(deskTopDto.getImageDto().getImageSystem()))
            ip=k8sService.createUbuntuDeskTop(userId,deskTopDto,network,podPort);
        else
            //ip=k8sService.createKylinDeskTop(userId,deskTopDto,network,podPort);
            ip="http://118.195.216.42:6080/vnc.html";
        //桌面容器添加到数据库中
        imageService.setDeskTop(userId,ip,deskTopDto);
        //该网络添加一个云桌面
        imageService.networkAddDeskTop(deskTopDto.getNetworkId());
        //用户添加一个云桌面
        imageService.userAddDeskTop(userId);
        //镜像增加一个使用数
        imageService.imageAddUse(deskTopDto.getImageDto().getImageId(),deskTopDto.getImageDto().getRecommendedId());
        return R.success("创建成功"+"ip:"+ip);
    }


    /**
     * 删除镜像
     * @param imageId-->镜像id
     * @return
     */
    @DeleteMapping("/delete/{userId}")
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
        log.info("imageDto:{}",imageDto);
        if(!TypeUtil.CheckConfig(imageDto.getRecommendedDataDisk()))return R.fail("配置错误，请重新配置");
        if(!TypeUtil.CheckResourceConfig(imageDto.getRecommendedCpu(),imageDto.getRecommendedMemory()))return R.fail("cpu和内存配置错误，请重新配置");
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
        //Integer GB= linuxService.computeStore(session);
        if(!TypeUtil.CheckConfig(imageDto.getRecommendedDataDisk()))return R.fail("配置错误，请重新配置");
        if(!TypeUtil.CheckResourceConfig(imageDto.getRecommendedCpu(),imageDto.getRecommendedMemory()))return R.fail("cpu和内存配置错误，请重新配置");
        if(imageService.ImageExist(imageDto))return  R.fail("镜像已存在");
        //标签为0时报错
        if(imageDto.getLabelName()==null)return R.fail("至少需要一个标签");
        imageService.updateImage(userId,imageDto);
        return R.success("修改镜像成功");
    }

    /**
     * 自定义镜像
     */
    @PostMapping("/updateCustom/{userId}")
    public R<Object> customImage(@PathVariable Integer userId,@RequestBody ImageDto imageDto){
        log.info("imageDto:{}",imageDto);
        if(!TypeUtil.CheckConfig(imageDto.getRecommendedDataDisk()))return R.fail("配置错误，请重新配置");
        if(!TypeUtil.CheckResourceConfig(imageDto.getRecommendedCpu(),imageDto.getRecommendedMemory()))return R.fail("cpu和内存配置错误，请重新配置");
        if(imageService.ImageExist(imageDto))return  R.fail("镜像已存在");
        if(imageDto.getLabelName()==null)return R.fail("至少需要一个标签");
        imageService.customImage(userId,imageDto);
        return R.success("自定义镜像成功");
    }
}
