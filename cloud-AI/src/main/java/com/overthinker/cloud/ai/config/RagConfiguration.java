package com.overthinker.cloud.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * RAG (Retrieval-Augmented Generation) 配置类
 * <p>
 * 配置 ChatClient Bean，用于检索增强生成相关的 AI 交互
 * </p>
 *
 * @author overthinker
 * @since 2024-01-15
 */
@Configuration
public class RagConfiguration {

    /**
     * 创建 RAG 专用的 ChatClient
     *
     * @param chatModel AI 聊天模型
     * @return 配置好的 ChatClient Bean
     */
    @Bean
    @Primary
    public ChatClient ragChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个智能助手，擅长根据提供的参考文档回答问题。请使用以下上下文信息来回答用户的问题：\n\n{context}")
                .defaultUser("{question}")
                .build();
    }
}