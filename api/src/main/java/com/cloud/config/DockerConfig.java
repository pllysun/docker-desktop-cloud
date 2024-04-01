package com.cloud.config;

import com.cloud.entity.ConfigEntity;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class DockerConfig {

    @Bean
    public DockerClient dockerClient() {
        DockerClient dockerClient = DockerClientBuilder.getInstance(ConfigEntity.Docker_Tcp).build();
        return dockerClient;
    }
}
