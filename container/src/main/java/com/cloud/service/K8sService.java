package com.cloud.service;

import com.cloud.DTO.ContainerDto;
import io.kubernetes.client.openapi.ApiException;

public interface K8sService {
    void openDeskTop(Integer userId,ContainerDto containerDto)throws ApiException;

    void closeDeskTop(Integer userId,ContainerDto containerDto) throws ApiException;

    void restartDeskTop(Integer userId, ContainerDto containerDto) throws ApiException;

    void deleteDeskTop(Integer userId,ContainerDto containerDto) throws ApiException;
}
