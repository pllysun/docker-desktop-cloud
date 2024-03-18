package com.cloud.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
public class K8sConfig {

    /**
     * 连接k8s客户端
     * @throws IOException
     */
    public static void k8sclient() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("config");
        Resource resource = new ClassPathResource("config");
        //获1.txt的取相对路径
        String classPathStr = classPathResource .getFile().getPath();
        System.out.println(classPathStr);
        String path = resource.getFile().getPath();
        ApiClient apiClient = Config.fromConfig(path);//配置文件位置
        // 设置默认 Api 客户端到配置
        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(apiClient);
    }

    /**
     * 组件api
     * @return
     * @throws Exception
     */
        @Bean
        public CoreV1Api apiClient() throws Exception {
            return new CoreV1Api();
        }

    /**
     *  部署api
     * @return
     * @throws Exception
     */
        @Bean
        public AppsV1Api appsV1Api() throws Exception{
            return new AppsV1Api();
        }

    /**
     *  网络api
     * @return
     */
    @Bean
    public NetworkingV1Api networkingV1Api(){
            return new NetworkingV1Api();
        }

}
