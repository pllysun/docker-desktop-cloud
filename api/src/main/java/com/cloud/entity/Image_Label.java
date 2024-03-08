package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("image_label")
public class Image_Label {
    /**
     * 镜像id
     */
    private String imageId;

    /**
     *  标签id
     */
    private Integer labelId;

    /**
     * 配置id
     */
    private Integer recommendedId;

}
