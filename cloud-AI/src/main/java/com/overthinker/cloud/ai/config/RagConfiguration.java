package com.overthinker.cloud.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RagConfiguration {

    @Bean
    @Primary
    public ChatClient ragChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个智能助手，擅长根据提供的参考文档回答问题。请使用以下上下文信息来回答用户的问题：\n\n{context}")
                .defaultUser("{question}")
                .build();
    }
}