package com.cloud.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    /**
     * 镜像ID
     */
    private Integer imageId;

    /**
     *  镜像名称
     */
    private String imageName;

    /**
     *  镜像系统
     */
    private String imageSystem;

    /**
     *  镜像标签
     */
    private List<String> labelName;

    /**
     *    推荐CPU
     */
    private Integer recommendedCpu;

    /**
     *    推荐内存
     */
    private Integer recommendedMemory;

    /**
     *    推荐系统盘
     */
    private Integer recommendedSystemDisk;

    /**
     *     推荐数据盘
     */
    private Integer recommendedDataDisk;

    /**
     *     镜像介绍
     */
    private String imageIntroduce;

}
