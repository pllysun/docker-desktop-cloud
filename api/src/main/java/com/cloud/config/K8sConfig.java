package com.cloud.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class K8sConfig {
    public static void k8sclient() throws IOException {
        ApiClient apiClient = Config.fromConfig("F:\\Java_code\\docker\\config");
        // 设置默认 Api 客户端到配置
        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(apiClient);
    }
        @Bean
        public CoreV1Api apiClient() throws Exception {
            return new CoreV1Api();
        }

        @Bean
        public AppsV1Api appsV1Api() throws Exception{
            return new AppsV1Api();
        }
}
