package com.cloud.DTO;

import lombok.Data;

@Data
public class DeskTopDto {

    /**
     * 网络id
     */
    String networkId;

    /**
     * 使用的镜像
     */
    ImageDto imageDto;

    /**
     * 显示名称（用户自定义）
     */
    String containerName;

    /**
     * 容器名称（唯一）
     */
    String podControllerName;

    /**
     * 端口(自动随机)
     */
    Integer podPort;
    /**
     * 密码
     */
    String password;
}
