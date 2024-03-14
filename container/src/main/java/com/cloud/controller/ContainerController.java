package com.cloud.controller;

import com.cloud.DTO.ContainerDto;
import com.cloud.DTO.PageBean;
import com.cloud.DTO.UserImageDto;
import com.cloud.entity.ConfigEntity;
import com.cloud.service.ContainerService;
import com.cloud.service.K8sService;
import com.cloud.service.LinuxService;
import com.cloud.utils.R;
import com.cloud.utils.TypeUtil;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.kubernetes.client.openapi.ApiException;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

/**
 * 桌面容器管理
 */
@RestController
@Slf4j
public class ContainerController {


    @Autowired
    private ContainerService containerService;

    @Autowired
    private K8sService k8sService;

    @Autowired
    private LinuxService linuxService;

    @Autowired
    private Session session;

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
     * @param userId（当前登录用户）
     * @return
     */
    @GetMapping("/list/{userId}")
    public R<Object> list(@PathVariable Integer userId) {
        List<ContainerDto> list = containerService.List(userId);
        return R.success(list);
    }


    /**
     * 开机
     * @param userId
     * @param containerDto
     * @return
     * @throws ApiException
     */
    @PutMapping("/open/{userId}")
    public R<Object> start(@PathVariable Integer userId,@RequestBody ContainerDto containerDto) throws ApiException {
        k8sService.openDeskTop(containerDto);
        containerService.openContainerState(userId,containerDto);
        return R.success("开机成功");
    }


    /**
     * 关机
     * @param userId
     * @param containerDto
     * @return
     * @throws ApiException
     */
    @PutMapping("/close/{userId}")
    public R<Object> close(@PathVariable Integer userId,@RequestBody ContainerDto containerDto) throws ApiException {
        log.info("userId:{},桌面容器:{}",userId,containerDto);
        k8sService.closeDeskTop(containerDto);
        containerService.closeContainerState(userId,containerDto);
        return R.success("关机成功");
    }


    /**
     * 重启
     * @param containerDto
     * @return
     * @throws ApiException
     */
    @PutMapping("/restart")
    public R<Object> restart(@RequestBody ContainerDto containerDto) throws ApiException {
        k8sService.restartDeskTop(containerDto);
        return R.success("重启成功");
    }

    /**
     * 重装系统
     * @param userId
     * @param containerDto
     * @return
     * @throws JSchException
     */
    @PutMapping("/reinstall/{userId}")
    public R<Object> reinstall(@PathVariable Integer userId,@RequestBody ContainerDto containerDto) throws JSchException {
        containerService.reinstall(userId,containerDto);
        linuxService.emptyNfsFile(containerDto,session);
        return R.success("重装成功");
    }

    /**
     * 扩容-->先修改pvc再修改pv,还有数据库
     */
    @PostMapping("/expansion/{userId}")
    public  R<Object> expansion(@PathVariable Integer userId,@RequestBody ContainerDto containerDto) throws ApiException, InterruptedException, JSchException, IOException {
        log.info("userId:{},桌面容器:{}",userId,containerDto);
        Integer GB= linuxService.computeStore(session);
        if(TypeUtil.CheckConfig(GB,containerDto.getPodControllerDataDisk()))return R.fail("扩容失败");
        //先关机
        close(userId,containerDto);
        k8sService.expansion(containerDto);
        containerService.updateDataDisk(userId,containerDto.getPodControllerId(),containerDto.getPodControllerDataDisk());
        return R.success("扩容成功");
    }

    /**
     * 上传
     * @param userImageDto
     * @return
     */
    @PostMapping("/upload")
    public  R<Object> upload(@RequestBody UserImageDto userImageDto) throws ApiException {
        //todo 选择标签，标签为0进行报错
        if(userImageDto.getLabelName()==null)return R.fail("必须选择一个标签");
        k8sService.upload(userImageDto);
        containerService.upload(userImageDto);
        return R.success("上传成功");
    }


    /**
     * 删除
     * @param userId
     * @param containerDto
     * @return
     * @throws ApiException
     * @throws JSchException
     */
    @DeleteMapping("/delete/{userId}")
    public  R<Object> delete(@PathVariable Integer userId,@RequestBody ContainerDto containerDto) throws ApiException, JSchException {
        //todo 定时一周自动删除-->查询的时候进行对照，然后进行删除
        containerService.deleteByPodControllerName(userId,containerDto);
        //todo 删除用户的云桌面和网络那里
        containerService.networkDeleteDeskTop(containerDto);
        containerService.userDeleteDeskTop(userId);
        k8sService.deleteDeskTop(containerDto);
        k8sService.deleteNfs(containerDto);
        linuxService.deleteNfsFile(containerDto,session);
        return R.success("删除成功");
    }

    /**
     * 修改桌面名称
     * @param userId
     * @param podControllerId
     * @param containerName
     * @return
     */
    @PostMapping("/update/{userId}/{podControllerId}")
    public R<Object> update(@PathVariable Integer userId,@PathVariable Integer podControllerId,String containerName){
        //todo 限制桌面名称长度
        if(containerName.length()> ConfigEntity.Container_Name_Limit)return R.fail("桌面名称长度超过限制");
        containerService.updateContainerName(userId,podControllerId,containerName);
        return R.success("更新成功");
    }

    /**
     * 管理员查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/adminList")
    //todo 管理员权限设置
    public R<Object> adminList(@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize){
        PageBean containerDtos=containerService.adminList(pageNum, pageSize);
        return  R.success(containerDtos);
    }
}