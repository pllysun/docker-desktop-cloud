package com.cloud.service;

import com.cloud.DTO.ContainerDto;
import com.cloud.DTO.DeskTopDto;
import com.cloud.DTO.ImageDto;
import com.cloud.entity.Network;
import com.jcraft.jsch.JSchException;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.stereotype.Service;


public interface K8sService {


    String createUbuntuDeskTop(Integer userId,  DeskTopDto deskTopDto, Network network,Integer podPort) throws ApiException, JSchException;

    String createKylinDeskTop(Integer userId, DeskTopDto deskTopDto, Network network, int podPort) throws ApiException;
}
