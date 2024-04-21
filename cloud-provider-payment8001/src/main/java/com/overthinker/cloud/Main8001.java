package com.overthinker.cloud;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.overthinker.cloud.mapper")
public class Main8001 {

    public static void main(String[] args) {
        SpringApplication.run(Main8001.class, args);
    }
}