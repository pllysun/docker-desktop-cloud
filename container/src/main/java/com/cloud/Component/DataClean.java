package com.cloud.Component;

import com.cloud.DTO.ContainerDto;
import com.cloud.controller.ContainerController;
import com.cloud.service.ContainerService;
import com.cloud.service.K8sService;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataClean {
    @Autowired
    ContainerService containerService;

    @Autowired
    K8sService k8sService;

    /**
     * 每天凌晨清理桌面数据
     * @throws ApiException
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanDeskTop() throws ApiException {
        // 清理桌面数据
        List<ContainerDto> containers = containerService.getTimeOutContainers();
        for (ContainerDto container : containers){
            containerService.deleteBySystem(container);
            k8sService.deleteDeskTop(container);
        }
    }


}
