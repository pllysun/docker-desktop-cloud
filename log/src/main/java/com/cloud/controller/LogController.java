package com.cloud.controller;
import com.cloud.DTO.LogDto;
import com.cloud.service.LogService;
import com.cloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/hello")
    public R<Object> hello() {
        return R.success("hello log");
    }

    /**
     * 查看该用户所有日志
     * @return
     */
    @GetMapping("/list/{userId}")
    public R<Object> list(@PathVariable Integer userId, @RequestParam(defaultValue = "1")Integer pageNum, @RequestParam(defaultValue = "10")Integer pageSize, String LogTypeName){
        log.info("userId:{},pageNum:{},pageSize:{},logTypeName:{}",userId,pageNum,pageSize,LogTypeName);
        List<LogDto> list=logService.listAll(pageNum,pageSize,userId,LogTypeName);
        return R.success(list);
    }

    /**
     * 管理员查看日志
     * @param pageNum
     * @param pageSize
     * @param LogTypeName
     * @return
     */
    @GetMapping("/managementList")
    //todo 限制管理员
    public R<Object> managementList(@RequestParam(defaultValue = "1")Integer pageNum, @RequestParam(defaultValue = "10")Integer pageSize, String LogTypeName){
        log.info("pageNum:{},pageSize:{},logTypeName:{}",pageNum,pageSize,LogTypeName);
        List<LogDto> list=logService.manageListAll(pageNum,pageSize,LogTypeName);
        return R.success(list);
    }
}
