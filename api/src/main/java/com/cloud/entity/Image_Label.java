package com.cloud.entity;

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
    private Integer imageId;

    /**
     *  标签id
     */
    private Integer labelId;
}
