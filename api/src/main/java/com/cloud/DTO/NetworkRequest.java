package com.cloud.DTO;

import com.cloud.entity.Network;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class NetworkRequest {
    /**
     * 网络对象
     */
    Network network;

    /**
     * HTTPRequest
     */
    HttpServletRequest request;
}
