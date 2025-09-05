package com.overthinker.cloud.ai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring AI 相关配置类。
 * <p>
 * 该类负责集中管理和配置 Spring AI 框架的核心组件，
 * 例如 {@link ChatClient} 的创建和自定义。
 * </p>
 */
@Configuration
@RequiredArgsConstructor
public class SpringAiConfiguration {

//    /**
//     * 创建并配置一个 {@link ChatClient} Bean。
//     * <p>
//     * {@link ChatClient} 是与大语言模型进行交互的推荐方式，它提供了一个流畅的、
//     * 方便的 API 来构建和发送请求。
//     * </p>
//     *
//     * @param ollamaChatModel Spring Boot 自动配置的 Ollama 聊天模型实例。
//     * @return 一个配置好的 {@link ChatClient} 实例，可用于应用程序的其他部分。
//     */
//    @Bean
//    public ChatClient anthropicChatClient(OllamaChatModel ollamaChatModel) {
//        return ChatClient.create(ollamaChatModel);
//    }
}
