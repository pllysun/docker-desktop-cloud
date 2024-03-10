package com.cloud.service.impl;

import com.cloud.entity.ConfigEntity;
import com.cloud.entity.Network;
import com.cloud.service.K8sService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.openapi.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class K8sServiceImpl implements K8sService {

    @Autowired
    private NetworkingV1Api networkingV1Api;

    @Override
    public void addNetwork(Network network) throws ApiException {
        //设置唯一网络策略名
        String soleName =network.getNetworkName()+network.getUserId()+ConfigEntity.Network_Name;
        log.info("addNetwork: {}",soleName);
        // 创建NetworkPolicy规范
        V1NetworkPolicySpec spec = new V1NetworkPolicySpec().podSelector(new V1LabelSelector()
                .matchLabels(Map.of(ConfigEntity.Pod_Selector_Key,network.getPodSelector())))
                .policyTypes(List.of(ConfigEntity.NetWork_policyTypes))
                .ingress(List.of(new V1NetworkPolicyIngressRule().from(List.of(new V1NetworkPolicyPeer().ipBlock(new V1IPBlock().cidr(network.getUserIp()))))));
        // 创建NetworkPolicy
        V1NetworkPolicy networkPolicy = new V1NetworkPolicy().spec(spec).metadata(new V1ObjectMeta().name(soleName).namespace(ConfigEntity.Image_NameSpace));
        // 调用API创建NetworkPolicy
        networkingV1Api.createNamespacedNetworkPolicy(ConfigEntity.Image_NameSpace, networkPolicy, null, null, null);
    }

    @Override
    public void updateNetwork(Network network,String oldName) throws ApiException {
        String  oldSoleName =oldName+network.getUserId();
        V1NetworkPolicy existingPolicy = networkingV1Api.readNamespacedNetworkPolicy(oldSoleName, ConfigEntity.Image_NameSpace, null, null, null);
        //获取某一个字段后修改
        String newSoleName =network.getNetworkName()+network.getUserId();
        existingPolicy.getMetadata().setName(newSoleName);
        //修改整体网络策略
        networkingV1Api.replaceNamespacedNetworkPolicy(oldSoleName, ConfigEntity.Image_NameSpace, existingPolicy, null, null, null);
    }

    @Override
    public void deleteNetwork(String networkName) throws ApiException {
        // 调用API删除NetworkPolicy
        // 删除网络策略
        networkingV1Api.deleteNamespacedNetworkPolicy(networkName, ConfigEntity.Image_NameSpace, null, null, null, null, null, null);
    }


}
