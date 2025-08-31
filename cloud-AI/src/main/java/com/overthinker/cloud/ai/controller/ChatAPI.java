package com.overthinker.cloud.ai.controller;


import com.overthinker.cloud.ai.clientTools.DateTimeTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class ChatAPI {

    final ChatClient chatClient;




    @GetMapping("/ai/weather")
    public String getWeather(@RequestParam("message") String message) {
        return chatClient.prompt()
            .user(message)
            .call()
            .content();
    }

    @GetMapping("/ai/datetime")
    public Mono<String> getDateTime(@RequestParam("message") String message) {
        return Mono.fromCallable(() ->
                chatClient.prompt()
                        .user(message)
                        .tools(new DateTimeTools()) // 假设 DateTimeTools 已注册并可用
                        .call()
                        .content()
        );
    }

//    获取ollma的模型列表

}
