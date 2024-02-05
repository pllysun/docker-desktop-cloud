package com.cloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * todo 负责均衡配置，可以在每个要启动的微服务都搞一个，但不知道是否必要（没有也能跑）
 * @author RongRongShanShan
 * 2023/7/4 20:14
 */
@Configuration
public class ConfigBean {//@Configuration=spring的 application.xml
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
