package com.cloud.service.impl;

import com.cloud.DTO.DeskTopDto;
import com.cloud.entity.ConfigEntity;
import com.cloud.entity.Network;
import com.cloud.service.K8sService;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class K8sServiceImpl implements K8sService {

    @Autowired
    AppsV1Api appsV1Api;

    @Autowired
    CoreV1Api coreV1Api;


    //todo 这里需要进行存储挂载修改
    @Override
    public String createDeskTop(Integer userId, DeskTopDto deskTopDto, Network network) throws ApiException {
        String soleName=deskTopDto.getPodControllerName()+"-"+userId;
        //todo 挂载存储

        //todo 创建pv（需要多个）
        V1PersistentVolume volume = new V1PersistentVolume();
        V1ObjectMeta metadata = new V1ObjectMeta();
        metadata.setName(soleName+ConfigEntity.PV_Name);
        log.info("metadata.getName():{}",metadata.getName());
        volume.setMetadata(metadata);
        V1PersistentVolumeSpec spec2 = new V1PersistentVolumeSpec();
        log.info("capacity:{}",deskTopDto.getImageDto().getRecommendedDataDisk()+ConfigEntity.Disk_Unit);
        spec2.setCapacity(Map.of(ConfigEntity.Capacity_Storage,new Quantity(deskTopDto.getImageDto().getRecommendedDataDisk()+ConfigEntity.Disk_Unit)));
        spec2.setAccessModes(List.of(ConfigEntity.AccessModes));
        spec2.setPersistentVolumeReclaimPolicy(ConfigEntity.PersistentVolumeReclaimPolicy);
        spec2.setNfs(new V1NFSVolumeSource().server(ConfigEntity.Host_Ip).path(ConfigEntity.NfsFileName+soleName));
        //安装配置
        volume.setSpec(spec2);
        //创建pv
        coreV1Api.createPersistentVolume(volume,null,null,null);

        //todo 创建pvc
        V1PersistentVolumeClaim pvc = new V1PersistentVolumeClaim();
        V1ObjectMeta metadata2 = new V1ObjectMeta();
        metadata2.setName(soleName+ConfigEntity.PVC_Name);
        V1PersistentVolumeClaimSpec spec3 = new V1PersistentVolumeClaimSpec();
        spec3.setAccessModes(List.of(ConfigEntity.AccessModes));
        spec3.setResources(new V1ResourceRequirements().requests(Collections.singletonMap(ConfigEntity.Capacity_Storage,new Quantity(deskTopDto.getImageDto().getRecommendedDataDisk()+ConfigEntity.Disk_Unit))));
        pvc.setMetadata(metadata2);
        pvc.setSpec(spec3);
        coreV1Api.createNamespacedPersistentVolumeClaim(ConfigEntity.Image_NameSpace,pvc,null,null,null);

        //todo 创建pod控制器
        V1Deployment deployment = new V1Deployment();
        //配置metadata
        V1ObjectMeta metadata1 = new V1ObjectMeta();
        //用户自己输入自己的容器配置名称
        metadata1.setName(soleName);
        deployment.setMetadata(metadata1);
        //配置spec
        V1DeploymentSpec spec1 = new V1DeploymentSpec();
        spec1.setRevisionHistoryLimit(ConfigEntity.RevisionHistoryLimit);//回退版本数设置
        spec1.setReplicas(ConfigEntity.Replicas);//副本数量
        //为pod设置cpu和memory-->记得来修改这里的绿色参数
        spec1.setSelector(new V1LabelSelector().matchLabels(Map.of(ConfigEntity.Pod_Selector_Key,network.getPodSelector())).matchLabels(Map.of(ConfigEntity.MatchLabels_Key,soleName)));
        spec1.setTemplate(new V1PodTemplateSpec().metadata(new V1ObjectMeta().labels(Map.of(ConfigEntity.Pod_Selector_Key,network.getPodSelector())).labels(Map.of(ConfigEntity.MatchLabels_Key,soleName)))
                .spec(new V1PodSpec().containers(Arrays.asList(new V1Container().name(soleName).image(deskTopDto.getImageDto().getImageName())
                .volumeMounts(Arrays.asList(new V1VolumeMount().name(ConfigEntity.VolumeName).mountPath(ConfigEntity.MountPath)))//这里的卷暂时写成常量，volumeName暂时写成常量
                .resources(new V1ResourceRequirements()
                .limits(Map.of(ConfigEntity.CPU, new Quantity(deskTopDto.getImageDto().getRecommendedCpu().toString()), ConfigEntity.Memory, new Quantity(deskTopDto.getImageDto().getRecommendedMemory()+ConfigEntity.Memory_Unit))))))
                .volumes(Arrays.asList(new V1Volume().name(ConfigEntity.VolumeName)
                .persistentVolumeClaim(new V1PersistentVolumeClaimVolumeSource()
                .claimName(soleName+ConfigEntity.PVC_Name)
                .readOnly(ConfigEntity.BoolReadOnly))))));
        //安装配置
        deployment.setSpec(spec1);
        //运行容器
        appsV1Api.createNamespacedDeployment(ConfigEntity.Image_NameSpace, deployment, null, null, null);

        //todo 开放端口(这里要随机端口)
        V1Service service = new V1Service();
        V1ObjectMeta metadata3 = new V1ObjectMeta();
        metadata3.setName(soleName+ConfigEntity.Service);
        metadata3.setNamespace(ConfigEntity.Image_NameSpace);
        service.setMetadata(metadata3);
        V1ServiceSpec spec = new V1ServiceSpec();
        spec.setType(ConfigEntity.Service_Type);
        V1ServicePort port = new V1ServicePort();//-->端口信息
        port.setName(ConfigEntity.Port_Name);//端口名
        port.setPort(ConfigEntity.Port_Port);//service端口
        port.setProtocol(ConfigEntity.Port_Protocol);//端口使用协议
        port.setTargetPort(new IntOrString(ConfigEntity.Port_TargetPort));//pod端口-->nginx
        spec.setPorts(Collections.singletonList(port));//主机端口
        spec.setSelector(Collections.singletonMap(ConfigEntity.MatchLabels_Key,soleName));//标签选择器，用于确定当前service代理哪些pod
        service.setSpec(spec);
        coreV1Api.createNamespacedService(ConfigEntity.Image_NameSpace, service, null, null, null);//创建service
        //获取service的nodeport
        V1Service service1 = coreV1Api.readNamespacedService(soleName+ConfigEntity.Service, ConfigEntity.Image_NameSpace, null,null,null);
        String nodePort = service1.getSpec().getPorts().get(0).getNodePort()+"";
        return ConfigEntity.Host_Ip+":"+nodePort;
    }
}