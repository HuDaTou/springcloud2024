package com.overthinker.cloud.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * 认证服务启动类
 * 负责启动Spring Authorization Server应用，提供OAuth2.1和OpenID Connect服务。
 */
@SpringBootApplication
@MapperScan("com.overthinker.cloud.auth.mapper")
@EnableDiscoveryClient // 启用Consul服务发现
public class CloudAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudAuthApplication.class, args);
    }

}