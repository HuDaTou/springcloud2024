package com.overthinker.cloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient //服务注册和发现
public class gateway9527 {
    public static void main(String[] args) {
        SpringApplication.run(gateway9527.class, args);
    }
}