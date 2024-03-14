package com.cloud.config;

import com.cloud.entity.ConfigEntity;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DockerConfig {

//    @Bean
//    public DockerClient dockerClient() {
//        return DockerClientBuilder.getInstance(ConfigEntity.Docker_Tcp).build();
//    }
}
