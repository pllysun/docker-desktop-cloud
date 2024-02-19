package com.cloud.service.impl;

import com.cloud.DTO.ContainerDto;
import com.cloud.entity.ConfigEntity;
import com.cloud.service.K8sService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class K8sServiceImpl implements K8sService {

    @Autowired
    private CoreV1Api coreV1Api;

    @Autowired
    private AppsV1Api appsV1Api;

    @Override
    public void openDeskTop(Integer userId,ContainerDto containerDto) throws ApiException {
        String soleName=containerDto.getPodControllerName()+"-"+userId;
        //更新pod控制器配置
        V1Deployment deployment=appsV1Api.readNamespacedDeployment(soleName,ConfigEntity.Image_NameSpace,null);
        //修改deployment的replica
        deployment.getSpec().setReplicas(ConfigEntity.Open_Replicas);
        appsV1Api.replaceNamespacedDeployment(soleName,ConfigEntity.Image_NameSpace,deployment,null,null,null,null);
    }

    @Override
    public void closeDeskTop(Integer userId,ContainerDto containerDto) throws ApiException {
        String soleName=containerDto.getPodControllerName()+"-"+userId;
        //log.info(soleName);
        //更新pod控制器配置
        V1Deployment deployment=appsV1Api.readNamespacedDeployment(soleName,ConfigEntity.Image_NameSpace,null);
        //修改deployment的replica
        deployment.getSpec().setReplicas(ConfigEntity.Close_Replicas);
        appsV1Api.replaceNamespacedDeployment(soleName,ConfigEntity.Image_NameSpace,deployment,null,null,null,null);
    }

    @Override
    public void restartDeskTop(Integer userId,ContainerDto containerDto) throws ApiException {
        String soleName=containerDto.getPodControllerName()+"-"+userId;
        //获取指定pod控制器中所有的pod
        V1PodList podlist=coreV1Api.listNamespacedPod(ConfigEntity.Image_NameSpace,
                null,null,null,null,ConfigEntity.MatchLabels_Key+"="+soleName,
                null,null,null,null,null);
        List<V1Pod> pod=podlist.getItems();
        for(V1Pod Pod:pod){
            coreV1Api.deleteNamespacedPod(Pod.getMetadata().getName(),ConfigEntity.Image_NameSpace,null,null,null,null,null,null);
        }
    }

    @Override
    public void deleteDeskTop(Integer userId,ContainerDto containerDto) throws ApiException {
        //删除指定的pod控制器
        String soleName=containerDto.getPodControllerName()+"-"+userId;
        appsV1Api.deleteNamespacedDeployment(soleName,ConfigEntity.Image_NameSpace,null,null,null,null,null,null);
        try {
            coreV1Api.deleteNamespacedService(soleName+ConfigEntity.Service,ConfigEntity.Image_NameSpace,null,null,null,null,null,null);
        }
        catch (Exception e) {
            System.out.println("傻逼api");
        }
    }
}
