package com.cloud.utils;

import com.cloud.DTO.*;
import com.cloud.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TypeUtil {

    /**
     * 初始化pod控制器
     * @param userId
     * @param ip
     * @return
     */
    public static PodController CreatePodController(Integer userId, DeskTopDto deskTopDto, String ip){
        PodController podController = new PodController();
        podController.setImageId(String.valueOf(deskTopDto.getImageDto().getImageId()));
        podController.setContainerName(deskTopDto.getContainerName());
        podController.setUserId(userId);
        podController.setNetworkId(deskTopDto.getNetworkId());//todo 这里的网络还没改
        podController.setIpAddress(ip);
        podController.setPodControllerName(deskTopDto.getPodControllerName());
        podController.setContainerState(ConfigEntity.Open_Container_Status);
        podController.setPodControllerCpu(deskTopDto.getImageDto().getRecommendedCpu());
        podController.setPodControllerMemory(deskTopDto.getImageDto().getRecommendedMemory());
        podController.setPodControllerDataDisk(deskTopDto.getImageDto().getRecommendedDataDisk());
        podController.setPodControllerVersion(ConfigEntity.Initial_Release);
        return podController;
    }

    /**
     *  生成唯一标识码，数字用X去表示
     */
    public static String generateLetterOnlyUUID() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replaceAll("-", "");
        return uuid.replaceAll("[0-9]", "X");
    }

    /**
     * 生成镜像类
     * @param imageDto
     * @param userId
     * @param imageName
     * @param imageId
     * @return
     */
    public static Image CreateImage(ImageDto imageDto, Integer userId,String imageName,String imageId) {
        Image image=new Image();
        image.setImageId(imageId);
        image.setImageName(imageName);
        image.setImageSystem(imageDto.getImageSystem());
        image.setSource(ConfigEntity.Image_Source);
        image.setImageRemark(imageDto.getImageRemark());
        image.setImageIntroduce(imageDto.getImageIntroduce());
        image.setUserId(userId);
        return image;
    }

    /**
     * 检查存储
     * @param GB
     * @param PodControllerDataDisk
     * @return
     */
    public static boolean CheckConfig(Integer GB,Integer PodControllerDataDisk){
        if(PodControllerDataDisk<=0
                ||PodControllerDataDisk>ConfigEntity.Disk_Top
                ||(PodControllerDataDisk>GB))return false;
        return true;
    }

    /**
     * 检查资源配置
     */
    public static boolean CheckResourceConfig(Integer CPU,Integer Memory){
        if(CPU<=0||Memory<=0||CPU>ConfigEntity.CPU_Top||Memory>ConfigEntity.Memory_Top)return false;
        return true;
    }

    /**
     * 变换为镜像类型
     * @param userImageDto
     * @return
     */
    public static Image CreateImage(UserImageDto userImageDto){
        Image image=new Image();
        image.setUserId(userImageDto.getContainerDto().getUserId());
        image.setImageName(userImageDto.getContainerDto().getPodControllerName());
        image.setSource(ConfigEntity.User_Source);
        image.setImageRemark(userImageDto.getImageRemark());
        image.setImageIntroduce(userImageDto.getImageIntroduce());
        image.setImageSystem(userImageDto.getImageSystem());
        return image;
    }

    /**
     * 创建时间-数据集
     * @param imageUseCount
     * @param deskCount
     * @return
     */
    public static List<Date_Value> CreateDateValue(List<DateImageUseCount> imageUseCount, List<DateDeskCount> deskCount,List<DateSumAndUseDto> dateSumAndUseDtos) {
        List<Date_Value> date_values = new ArrayList<>();
        for(int i=0;i<imageUseCount.size();i++){
            Date_Value date_value=new Date_Value();
            date_value.setDateTime(imageUseCount.get(i).getDateTime());
            date_value.setDeskCount(deskCount.get(i).getDeskCount());
            date_value.setImageUseCount(imageUseCount.get(i).getImageUseCount());
            date_value.setDeskUse(dateSumAndUseDtos.get(i).getUseNum());
            date_values.add(date_value);
        }
        return  date_values;
    }
}
