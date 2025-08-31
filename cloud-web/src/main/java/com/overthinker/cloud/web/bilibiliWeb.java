package com.overthinker.cloud.web;


import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@SpringBootApplication(scanBasePackages = "com.overthinker.cloud.web")
//@MapperScan(basePackages = {"com.overthinker.cloud.web.mapper"})
@Log4j2
@EnableDiscoveryClient
@RefreshScope //consul配置动态刷新
@EnableScheduling
@EnableMethodSecurity
public class bilibiliWeb {


    public static void main(String[] args) {
        SpringApplication.run(bilibiliWeb.class, args);


        log.info(
                """
                        后端启动成功
                        """
        );

    }
}

