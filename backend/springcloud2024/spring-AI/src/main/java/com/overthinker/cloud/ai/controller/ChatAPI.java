package com.overthinker.cloud.ai.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatAPI {

    final ChatClient chatClient;

    @GetMapping("/ai/chat")
    public String chat(@RequestParam(value = "message") String message) {
        return chatClient.chat(message);
    }
}
