package com.cloud.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserImageDto {
    /**
     * 容器请求类型
     */
    ContainerDto containerDto;
    /**
     * 标签
     */
    List<String> labelName;
    /**
     *  镜像名称
     */
    String imageRemark;
    /**
     * 镜像介绍
     */
    String imageIntroduce;
    /**
     * 操作系统
     */
    String imageSystem;

}
