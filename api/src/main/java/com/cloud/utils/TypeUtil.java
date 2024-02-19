package com.cloud.utils;

import com.cloud.DTO.DeskTopDto;
import com.cloud.DTO.ImageDto;
import com.cloud.entity.ConfigEntity;
import com.cloud.entity.PodController;

import java.util.Random;

public class TypeUtil {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ID_LENGTH = 10;
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
        podController.setNetworkId("1");
        podController.setIpAddress(ip);
        podController.setPodControllerName(deskTopDto.getPodControllerName());
        podController.setContainerState(ConfigEntity.ContainerStatus);
        podController.setPodControllerCpu(deskTopDto.getImageDto().getRecommendedCpu());
        podController.setPodControllerMemory(deskTopDto.getImageDto().getRecommendedMemory());
        podController.setPodControllerDataDisk(deskTopDto.getImageDto().getRecommendedDataDisk());
        podController.setPodControllerSystemDisk(deskTopDto.getImageDto().getRecommendedSystemDisk());
        podController.setPodControllerVersion(ConfigEntity.InitialRelease);
        return podController;
    }
}
