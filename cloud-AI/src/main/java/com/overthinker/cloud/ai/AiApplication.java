package com.overthinker.cloud.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Cloud AI 服务启动类
 * <p>
 * 负责启动 AI 微服务，提供大语言模型集成和 RAG 功能
 * </p>
 *
 * @author overthinker
 * @since 2024-01-15
 */
@SpringBootApplication
public class AiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
    }
}
