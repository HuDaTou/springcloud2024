package com.overthinker.cloud.ai;


import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Log4j2

@EnableScheduling
@SpringBootApplication
public class AiApplication {


    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
    }
}
