package com.cloud.controller;

import com.cloud.DTO.DateImageUseCount;
import com.cloud.DTO.DateSumAndUseDto;
import com.cloud.DTO.DeskTopControlDto;
import com.cloud.entity.ConfigEntity;
import com.cloud.service.ControlService;
import com.cloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
//todo 未测试
@RestController
@Slf4j
public class ControlController {

    @Autowired
    private ControlService controlService;
    @GetMapping("/hello")
    public R<Object> hello() {
        return  R.success("Hello, World!");
    }

    /**
     * 使用中+关机中
     * @param userId
     * @return
     */
    @GetMapping("/deskTopUser/{userId}")
    public R<Object> deskTopControl(@PathVariable Integer userId) {
        log.info("userId:{}",userId);
        DeskTopControlDto deskTopControlDto= controlService.getDeskTopControl(userId);
        return R.success(deskTopControlDto);
    }

    /**
     * 获取镜像使用情况
     * @param userId
     * @return
     */
    @GetMapping("/imageUse/{userId}")
    public R<Object> imageUse(@PathVariable Integer userId) {
        List<DateImageUseCount> list=controlService.getImageUseCount(userId);
        log.info("获取镜像使用情况:{}",list);
        return  R.success(list);
    }


    /**
     *  获取过期比例
     * @param userId
     * @return
     */
    @GetMapping("/expireProportion/{userId}")
    public R<Object> expireProportion(@PathVariable Integer userId) {
        log.info("userId{}",userId);
        Integer proportion=controlService.getExpireProportion(userId);
        return  R.success(proportion);
    }

    /**
     * 总数+使用数
     */
    @GetMapping("/sumAndUse/{userId}")
    public R<Object> sumAndUse(@PathVariable Integer userId) {
        List<DateSumAndUseDto> sumAndUseDto=controlService.getSumAndUse(userId);
        return  R.success(sumAndUseDto);
    }

    /**
     * 管理员搜索所有使用中+关机中
     * @return
     */
    @GetMapping("/adminDeskTopUser")
    //todo 管理员
    public R<Object> adminDeskTopControl() {
        DeskTopControlDto deskTopControlDto= controlService.getDeskTopControl(null);
        return R.success(deskTopControlDto);
    }



}