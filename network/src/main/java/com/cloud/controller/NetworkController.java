package com.cloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cloud.entity.ConfigEntity;
import com.cloud.entity.Network;
import com.cloud.mapper.LabelMapper;
import com.cloud.service.K8sService;
import com.cloud.service.NetworkService;
import com.cloud.utils.R;
import com.cloud.utils.TypeUtil;
import io.kubernetes.client.openapi.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class NetworkController {

    @Autowired
    private NetworkService networkService;

    @Autowired
    private K8sService k8sService;

    /**
     * 测试接口
     * @return
     */
    @GetMapping("/hello")
    public R<Object> hello() {
        return R.success("hello network");
    }

    /**
     * 返回该用户网络数据
     * @param userId
     * @return
     */
    @GetMapping("/list/{userId}")
    public R<Object> list(@PathVariable Integer userId) {
        LambdaQueryWrapper<Network> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Network::getUserId, userId);
        List<Network> list = networkService.list(lambdaQueryWrapper);
        return R.success(list);
    }


    /**
     * 添加网络数据
     * @param network
     * @return
     */
    @PostMapping("/addNetwork/{userId}")
    public R<Object> add(@PathVariable Integer userId,@RequestBody Network network,HttpServletRequest request) throws ApiException, UnknownHostException {
        //todo 限制网络名称，防止重复网络
        if(networkService.networkExist(network))
            return R.fail("桌面标签已存在");
        log.info("request:{}",request.getHeader("X-Real-IP"));
        //todo 获取用户ip
        String userIP= TypeUtil.IpToNetWork(network.getUserIp());
        network.setUserIp(userIP);
        network.setPodCount(0);
        network.setUserId(userId);
        //生成唯一id
        network.setNetworkId(UUID.randomUUID().toString());
        //保存网络
        networkService.save(network);
        networkService.log(network.getUserId(),ConfigEntity.Create_Network_Log_Type,ConfigEntity.Create_Network_Log_Content+network.getNetworkName());
        //todo k8s服务
        //k8sService.addNetwork(network);
        return R.success("添加成功");
    }

    /**
     * 编辑网络名称
     */
    @PutMapping("/updateNetwork")
    public R<Object> edit(@RequestBody Network network) throws ApiException {
        String oldName = networkService.getById(network.getNetworkId()).getNetworkName();
        networkService.updateById(network);
        networkService.log(network.getUserId(),ConfigEntity.Update_Network_Log_Type,ConfigEntity.Update_Network_Log_Content(oldName)+network.getNetworkName());
        //todo k8s服务
        return R.success("编辑成功");
    }

    /**
     * 删除网络数据
     */
    @DeleteMapping("/deleteNetwork/{networkId}")
    public R<Object> delete(@PathVariable String networkId) throws ApiException {
        Network network=networkService.getById(networkId);
        networkService.removeById(networkId);
        networkService.log(network.getUserId(),ConfigEntity.Delete_Network_Log_Type,ConfigEntity.Delete_Network_Log_Content+network.getNetworkName());
        //todo k8s服务
        //k8sService.deleteNetwork(network);
        return R.success("删除成功");
    }

    /**
     * 查询所有网络数据
     * @return
     */
    @GetMapping("/listAll")
    public R<Object> listAll() {
        List<Network> list = networkService.list();
        return R.success(list);
    }
}