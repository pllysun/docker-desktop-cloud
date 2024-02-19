package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("image_recommended")
public class Image_Recommended {
    /**
     * 镜像id
     */
    private String imageId;

    /**
     *  推荐镜像id
     */
    private String recommendedId;
}
