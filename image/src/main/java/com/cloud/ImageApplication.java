package com.cloud;

import com.cloud.config.K8sConfig;
import com.cloud.config.LinuxConfig;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.Config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.cloud.mapper") // todo mapper扫描可以写着-->mybatis plus
public class ImageApplication {
    public static void main(String[] args) throws IOException {
        K8sConfig.k8sclient();
        SpringApplication.run(ImageApplication.class, args);
    }
}