package com.cloud;

import com.cloud.config.K8sConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.IOException;


@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.cloud.mapper") // todo mapper扫描可以写着-->mybatis plus
public class NetworkApplication {
    public static void main(String[] args) throws IOException {
        K8sConfig.k8sclient();
        SpringApplication.run(NetworkApplication.class, args);
    }
}