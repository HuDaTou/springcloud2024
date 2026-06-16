package com.overthinker.cloud.media;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@MapperScan("com.overthinker.cloud.media.mapper")
@EnableDiscoveryClient // 启用Consul服务发现
public class Media {
    public static void main(String[] args) {
        SpringApplication.run(Media.class, args);

    }
}