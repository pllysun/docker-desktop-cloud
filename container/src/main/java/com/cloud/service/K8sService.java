package com.cloud.service;

import com.cloud.DTO.ContainerDto;
import com.cloud.DTO.UserImageDto;
import io.kubernetes.client.openapi.ApiException;

public interface K8sService {
    void openDeskTop(ContainerDto containerDto)throws ApiException;

    void closeDeskTop(ContainerDto containerDto) throws ApiException;

    void restartDeskTop( ContainerDto containerDto) throws ApiException;

    void deleteDeskTop(ContainerDto containerDto) throws ApiException;

    void deleteNfs(ContainerDto containerDto) throws ApiException;

    void expansion(ContainerDto containerDto) throws ApiException, InterruptedException;

    void upload(UserImageDto userImageDto);
}
