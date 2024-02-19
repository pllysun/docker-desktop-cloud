package com.cloud.service;

import com.cloud.DTO.ContainerDto;
import com.cloud.DTO.DeskTopDto;
import com.cloud.DTO.ImageDto;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.stereotype.Service;


public interface K8sService {


    String createDeskTop(Integer userId,  DeskTopDto deskTopDto) throws ApiException;
}
