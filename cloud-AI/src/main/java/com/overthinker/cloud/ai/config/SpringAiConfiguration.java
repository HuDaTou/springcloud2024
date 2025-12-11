package com.overthinker.cloud.ai.config;

import com.overthinker.cloud.ai.enums.MyModel;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class SpringAiConfiguration {

    public  final String baseUrl = "http://localhost:11434";


    @Bean
    public OllamaApi chatCompletionApi() {

        return OllamaApi.builder().baseUrl(baseUrl).build();
    }

    @Bean(name = "gemma3")
    public ChatModel gemma3(OllamaApi ollamaApi){
        return OllamaChatModel
                .builder().ollamaApi(ollamaApi).defaultOptions(OllamaOptions.builder().model(MyModel.GEMMA3.id()).temperature(0.4).build()).build();
    }

    @Bean
    public ChatClient chatClient(@Qualifier("gemma3") ChatModel chatModel){
        return ChatClient.builder(chatModel).build();
    }
}
