package com.cloud.service.impl;
import com.cloud.DTO.ContainerDto;
import com.cloud.DTO.UserImageDto;
import com.cloud.entity.ConfigEntity;
import com.cloud.service.K8sService;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientImpl;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.proto.V1Apps;
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
    private CoreV1Api coreV1Api;

    @Autowired
    private AppsV1Api appsV1Api;

    @Autowired
    private DockerClient dockerClient;

    @Override
    public void openDeskTop(ContainerDto containerDto) throws ApiException {
        String soleName=containerDto.getPodControllerName()+"-"+containerDto.getUserId();
        //更新pod控制器配置
        V1Deployment deployment=appsV1Api.readNamespacedDeployment(soleName,ConfigEntity.Image_NameSpace,null,null,null);
        //修改deployment的replica
        deployment.getSpec().setReplicas(ConfigEntity.Open_Replicas);
        appsV1Api.replaceNamespacedDeployment(soleName,ConfigEntity.Image_NameSpace,deployment,null,null,null);
    }

    @Override
    public void closeDeskTop(ContainerDto containerDto) throws ApiException {
        String soleName=containerDto.getPodControllerName()+"-"+containerDto.getUserId();
        //log.info(soleName);
        //更新pod控制器配置
        V1Deployment deployment=appsV1Api.readNamespacedDeployment(soleName,ConfigEntity.Image_NameSpace,null,null,null);
        //修改deployment的replica
        deployment.getSpec().setReplicas(ConfigEntity.Close_Replicas);
        appsV1Api.replaceNamespacedDeployment(soleName,ConfigEntity.Image_NameSpace,deployment,null,null,null);
    }


    @Override
    public void restartDeskTop(ContainerDto containerDto) throws ApiException {
        String soleName=containerDto.getPodControllerName()+"-"+containerDto.getUserId();
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
    public void deleteDeskTop(ContainerDto containerDto) throws ApiException {
        //删除指定的pod控制器
        String soleName=containerDto.getPodControllerName()+"-"+containerDto.getUserId();
        log.info("删除桌面，删除的pod控制器名称是{}",soleName);
        appsV1Api.deleteNamespacedDeployment(soleName,ConfigEntity.Image_NameSpace,null,null,null,null,null,null);
        try {
            coreV1Api.deleteNamespacedService(soleName+ConfigEntity.Service,ConfigEntity.Image_NameSpace,null,null,null,null,null,null);
        }
        catch (Exception e) {
            System.out.println("傻逼api");
        }
    }


    @Override
    public void deleteNfs(ContainerDto containerDto) throws ApiException {
        //删除pvc
        String soleName=containerDto.getPodControllerName()+"-"+containerDto.getUserId();
        log.info("name:{}",soleName+ConfigEntity.PVC_Name);
        coreV1Api.deleteNamespacedPersistentVolumeClaim(soleName+ConfigEntity.PVC_Name,ConfigEntity.Image_NameSpace,null,null,null,null,null,null);
        //删除pv
        coreV1Api.deletePersistentVolume(soleName+ConfigEntity.PV_Name,null,null,null,null,null,null);
    }


    //todo 要进行修改
    @Override
    public void expansion(ContainerDto containerDto) throws ApiException, InterruptedException {
        String soleName=containerDto.getPodControllerName()+"-"+containerDto.getUserId();
        deleteNfs(containerDto);
        Thread.sleep(120);
        //创建pv
        V1PersistentVolume volume = new V1PersistentVolume();
        V1ObjectMeta metadata = new V1ObjectMeta();
        metadata.setName(soleName+ConfigEntity.PV_Name);
        //log.info("metadata.getName():{}",metadata.getName());
        volume.setMetadata(metadata);
        V1PersistentVolumeSpec spec2 = new V1PersistentVolumeSpec();
        //log.info("capacity:{}",containerDto.getPodControllerSystemDisk()+ConfigEntity.Disk_Unit);
        spec2.setCapacity(Map.of(ConfigEntity.Capacity_Storage,new Quantity(containerDto.getPodControllerDataDisk()+ConfigEntity.Disk_Unit)));
        spec2.setAccessModes(List.of(ConfigEntity.AccessModes));
        spec2.setPersistentVolumeReclaimPolicy(ConfigEntity.PersistentVolumeReclaimPolicy);
        spec2.setNfs(new V1NFSVolumeSource().server(ConfigEntity.Nfs_Ip).path(ConfigEntity.NfsFileName+soleName));
        //安装配置
        volume.setSpec(spec2);
        //创建pv
        coreV1Api.createPersistentVolume(volume,null,null,null);
        //创建pvc
        V1PersistentVolumeClaim pvc = new V1PersistentVolumeClaim();
        V1ObjectMeta metadata2 = new V1ObjectMeta();
        metadata2.setName(soleName+ConfigEntity.PVC_Name);
        V1PersistentVolumeClaimSpec spec3 = new V1PersistentVolumeClaimSpec();
        spec3.setAccessModes(List.of(ConfigEntity.AccessModes));
        spec3.setResources(new V1ResourceRequirements().requests(Collections.singletonMap(ConfigEntity.Capacity_Storage,new Quantity(containerDto.getPodControllerDataDisk()+ConfigEntity.Disk_Unit))));
        pvc.setMetadata(metadata2);
        pvc.setSpec(spec3);
        coreV1Api.createNamespacedPersistentVolumeClaim(ConfigEntity.Image_NameSpace,pvc,null,null,null);
        //修改deployment
        V1Deployment deployment=appsV1Api.readNamespacedDeployment(soleName,ConfigEntity.Image_NameSpace,null,null,null);
        //修改deployment的replica
        deployment.getSpec().getTemplate().getSpec().setVolumes(Arrays.asList(new V1Volume().name(ConfigEntity.VolumeName).persistentVolumeClaim(new V1PersistentVolumeClaimVolumeSource().claimName(soleName+ConfigEntity.PVC_Name))));
        appsV1Api.replaceNamespacedDeployment(soleName,ConfigEntity.Image_NameSpace,deployment,null,null,null);
    }

    @Override
    public void upload(UserImageDto userImageDto) throws ApiException {
        // 先利用k8s的api获取容器ip
        String solename=userImageDto.getContainerDto().getPodControllerName()+"-"+userImageDto.getContainerDto().getUserId();
        V1Deployment deployment=appsV1Api.readNamespacedDeployment(solename,ConfigEntity.Image_NameSpace,null,null,null);
        String labelSelector = deployment.getSpec().getSelector().getMatchLabels().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((first, second) -> first + "," + second)
                .orElse("");//获取标签选择器
        log.info("labelSelector:{}",labelSelector);
        V1PodList podList = coreV1Api.listNamespacedPod(ConfigEntity.Image_NameSpace, null, null, null, null, labelSelector, null, null, null, null, null);
        String containerID = null;
        for (V1Pod item : podList.getItems()) {
            //System.out.println("Pod Name: " + item.getMetadata().getName());
            // 遍历每个容器
            containerID = item.getStatus().getContainerStatuses().get(0).getContainerID();//获取容器id
        }
        log.info("containerID:{}",containerID);
        int i = 0;//“/”的个数
        for (char c : containerID.toCharArray()) {
            if (c == '/'){
                i++;
            }
            if (i == 2) {
                containerID = containerID.substring(containerID.indexOf("/") + 2);
                break;
            }
        }//获取容器id
        System.out.println(containerID);
        String newImageName=userImageDto.getContainerDto().getPodControllerName()+userImageDto.getContainerDto().getUserId()+ConfigEntity.Image;
        if (containerID != null) {
            dockerClient.commitCmd(containerID).withRepository(newImageName).exec();//镜像名称为pod_controller_id
        }

    }
}