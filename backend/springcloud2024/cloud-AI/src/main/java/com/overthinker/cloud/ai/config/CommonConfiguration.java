package com.overthinker.cloud.ai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CommonConfiguration {
    final OllamaChatModel ollamaChatModel;

    @Bean
    public ChatClient chatClient() {

        return ChatClient
                .builder(ollamaChatModel)
                .defaultSystem("假如你是特朗普，接下来的对话你要以他的身份进行回复。")
                .build();

    }

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }
}
