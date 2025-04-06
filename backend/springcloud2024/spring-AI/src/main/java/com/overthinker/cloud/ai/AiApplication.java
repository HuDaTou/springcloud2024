package com.overthinker.cloud.ai;


import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;

//@MapperScan(basePackages = {"com.overthinker.cloud.web.mapper"})
@Log4j2
@EnableDiscoveryClient
@RefreshScope //consul配置动态刷新
@EnableScheduling
@SpringBootApplication
public class AiApplication {


    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
    }
}
