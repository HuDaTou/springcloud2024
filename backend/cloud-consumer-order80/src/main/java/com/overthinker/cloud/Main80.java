package com.overthinker.cloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//该注解用户向使用consul或者zookeeper作为注册中心时注册服务
@EnableDiscoveryClient
//该注解用于开启Feign功能
@EnableFeignClients(basePackages = {"com.overthinker.cloud.apis"})
public class Main80 {
    public static void main(String[] args) {
        SpringApplication.run(Main80.class, args);

    }
}
