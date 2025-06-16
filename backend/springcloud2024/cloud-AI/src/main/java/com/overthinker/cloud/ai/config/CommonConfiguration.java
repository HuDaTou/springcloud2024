package com.overthinker.cloud.ai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class CommonConfiguration {

    @Bean
    public ChatClient ollamaChatclient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)

            .build();
    }

//    @Bean
//    public ChatClient anthropicChatClient(OllamaChatModel ollamaChatModel) {
//        return ChatClient.create(ollamaChatModel);

//    }

}
