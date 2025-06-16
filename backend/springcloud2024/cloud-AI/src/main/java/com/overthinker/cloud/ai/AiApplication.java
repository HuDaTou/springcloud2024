package com.overthinker.cloud.ai;


import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Log4j2

@EnableScheduling
@SpringBootApplication
@EnableR2dbcRepositories //开启R2dbcRepository支持 仓库功能  jpa
public class AiApplication {


    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
    }
}
