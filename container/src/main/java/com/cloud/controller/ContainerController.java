package com.cloud.controller;

import com.cloud.DTO.ContainerDto;
import com.cloud.service.ContainerService;
import com.cloud.service.K8sService;
import com.cloud.utils.R;
import io.kubernetes.client.openapi.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 桌面容器管理
 */
@RestController
@Slf4j
@RequestMapping("/container")
public class ContainerController {


    @Autowired
    private ContainerService containerService;

    @Autowired
    private K8sService k8sService;
    /**
     * 测试接口
     * @return
     */
    @GetMapping("/hello")
    public R<Object> hello() {
        return R.success("Hello from Nacos-Config-Client");
    }


    /**
     *  获取桌面容器列表
     * @param userId
     * @return
     */
    @GetMapping("/list/{userId}")
    public R<Object> list(@PathVariable Integer userId){
        List<ContainerDto> list = containerService.List(userId);
        return R.success(list);
    }

    /**
     * 开机
     */
    @PutMapping("/open/{userId}")
    public R<Object> start(@PathVariable Integer userId,@RequestBody ContainerDto containerDto) throws ApiException {
        k8sService.openDeskTop(userId,containerDto);
        containerService.openContainerState(containerDto);
        return R.success("开机成功");
    }


    /**
     * 关机
     */
    @PutMapping("/close/{userId}")
    public R<Object> close(@PathVariable Integer userId,@RequestBody ContainerDto containerDto) throws ApiException {
        log.info("userId:{},桌面容器:{}",userId,containerDto);
        k8sService.closeDeskTop(userId,containerDto);
        containerService.closeContainerState(containerDto);
        return R.success("关机成功");
    }


    /**
     * 重启
     */
    @PutMapping("/restart/{userId}")
    public R<Object> restart(@PathVariable Integer userId,@RequestBody ContainerDto containerDto) throws ApiException {
        k8sService.restartDeskTop(userId,containerDto);
        return R.success("重启成功");
    }


    /**
     * 重装系统-->直接把存储删了，然后重启
     */
    @PutMapping("/reinstall")
    public R<Object> reinstall(@RequestBody ContainerDto containerDto){

        return R.success("重装成功");
    }


    /**
     * 快照-->先获取id再commit(不好写)
     */
    @PutMapping("/snapshot")
    public R<Object> snapshot(@RequestBody ContainerDto containerDto){

        return R.success("快照成功");
    }


    /**
     * 扩容-->先修改pvc再修改pv
     */
    @PutMapping("/expansion")
    public  R<Object> expansion(@RequestBody ContainerDto containerDto){

        return R.success("扩容成功");
    }


    /**
     * 上传-->先获取id再commit
     */
    @PutMapping("/upload")
    public  R<Object> upload(@RequestBody ContainerDto containerDto){

        return R.success("上传成功");
    }


    /**
     * 删除-->id直接删，先删k8s再删数据库(没写完，存储问题)
     */
    @DeleteMapping("/delete/{userId}")
    public  R<Object> delete(@PathVariable Integer userId,@RequestBody ContainerDto containerDto) throws ApiException {
        containerService.deleteByPodControllerName(containerDto);
        k8sService.deleteDeskTop(userId,containerDto);
        return R.success("删除成功");
    }

    /**
     * 修改桌面名称
     * @param  containerName
     * @return
     */
    @PostMapping("/update/{podControllerId}")
    public R<Object> update(@PathVariable Integer podControllerId,String containerName){
        containerService.updateContainerName(podControllerId,containerName);
        return R.success("更新成功");
    }
}