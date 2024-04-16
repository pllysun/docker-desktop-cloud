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
     * 镜像ID(不显示)
     */
    private String imageId;

    /**
     *  镜像名称（不显示）
     */
    private String imageName;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     *  镜像描述
     */
    private String imageRemark;

    /**
     *  镜像系统
     */
    private String imageSystem;

    /**
     *  镜像标签
     */
    private List<String> labelName;

    /**
     * 镜像配置id
     */
    private Integer recommendedId;

    /**
     *    推荐CPU
     */
    private Integer recommendedCpu;

    /**
     *    推荐内存
     */
    private Integer recommendedMemory;


    /**
     *     推荐数据盘
     */
    private Integer recommendedDataDisk;

    /**
     *     镜像介绍
     */
    private String imageIntroduce;

    /**
     * 镜像图片
     */
    private Integer imagePhoto;

}
