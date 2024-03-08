package com.cloud.service;

import com.cloud.entity.Network;
import io.kubernetes.client.openapi.ApiException;

public interface K8sService {
    void addNetwork(Network network) throws ApiException;

    void updateNetwork(Network network,String oldName) throws ApiException;

    void deleteNetwork(String networkName) throws ApiException;
}
