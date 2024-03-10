package com.cloud;

import com.cloud.config.K8sConfig;
import com.cloud.config.LinuxConfig;
import com.jcraft.jsch.JSchException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication
@MapperScan("com.cloud.mapper") // todo mapper扫描可以写着-->mybatis plus
public class ContainerApplication {

    public static void main(String[] args) throws IOException, JSchException {
        K8sConfig.k8sclient();
        SpringApplication.run(ContainerApplication.class, args);
    }
}