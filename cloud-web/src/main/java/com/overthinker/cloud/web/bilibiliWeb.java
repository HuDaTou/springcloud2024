package com.overthinker.cloud.web;


import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(scanBasePackages = "com.overthinker.cloud.web")
@MapperScan(basePackages = {"com.overthinker.cloud.web.mapper"})
@Log4j2
public class bilibiliWeb {
//    当项目启动时控制台输出项目启动成功的日志
    static {
        log.info("项目启动成功");
    }

    public static void main(String[] args) {
        SpringApplication.run(bilibiliWeb.class, args);
    }
}

