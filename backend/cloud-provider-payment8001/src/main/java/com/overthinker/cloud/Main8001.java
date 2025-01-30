package com.overthinker.cloud;


import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@MapperScan("com.overthinker.cloud.mapper")
@Log4j2
@EnableDiscoveryClient
@RefreshScope
public class Main8001 {
//    当项目启动时控制台输出项目启动成功的日志
    static {
        log.info("项目启动成功");
    }

    public static void main(String[] args) {
        SpringApplication.run(Main8001.class, args);
    }
}

