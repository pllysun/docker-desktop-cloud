package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.InaccessibleObjectException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("image_table")
public class Image {
    /**
     * 镜像id
     */
    @TableId
    private  String imageId;

    /**
     * 镜像名称
     */
    private  String imageName;

    /**
     * 镜像系统
     */
    private  String imageSystem;

    /**
     * 镜像归属
     */
    private Integer source;

    /**
     * 镜像介绍
     */
    private String imageIntroduce;

    /**
     * 镜像备注
     */
    private String imageRemark;

    /**
     * 上传用户
     */
    private Integer userId;
}
