package com.overthinker.cloud.ai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@RequiredArgsConstructor
public class SpringAiConfiguration {

    public  final String baseUrl = "localhost:12434/engines";

//    @Bean
//    @Primary
//    public OpenAiApi openAiApi1() {
//        return new OpenAiApi("http://localhost:12434/engines/v1", "test");
//    }
//
//    @Bean
//    @Primary
//    public ChatModel chatModel1(@Qualifier("openAiApi1") OpenAiApi openAiApi) {
//        return new OpenAiChatModel(openAiApi);
//    }
//
//
//    @Bean
//    public Map<String, ChatModel> chatModelMap(
//            @Qualifier("chatModel1") ChatModel chatModel1) {
//        Map<String, ChatModel> chatModelMap = new ConcurrentHashMap<>();
//        chatModelMap.put("model1", chatModel1);
//
//        return chatModelMap;
//    }
//
//    @Bean
//    public ChatClient chatClient(@Qualifier("chatModel1") ChatModel chatModel) {
//        return ChatClient.builder(chatModel).build();
//    }

    @Bean
    public OpenAiApi chatCompletionApi() {

        return OpenAiApi.builder().baseUrl(baseUrl).apiKey("test").build();
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel){
        return ChatClient.builder(chatModel).build();
    }
}
