package com.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * GateWayApplication
 *
 * @author zhou kai
 * 2023-12-02
 **/
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.cloud.mapper") // todo mapper扫描可以写着
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
