package com.overthinker.cloud.ai.service.impl;

import com.overthinker.cloud.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class AiServiceImpl implements AiService {

    private final Map<String, ChatModel> chatModelMap;
    private ChatClient chatClient;

    @Override
    public String changeModel(String modelId) {
        ChatModel chatModel = chatModelMap.get(modelId);
        if (chatModel != null) {
            this.chatClient = ChatClient.builder(chatModel).build();
            return "Chat model switched to: " + modelId;
        } else {
            return "Error: Model not found";
        }
    }

    public String chat(String prompt) {
        if (chatClient == null) {
            return "Error: Chat model not selected";
        }
        return chatClient.prompt().user(prompt).call().content();
    }
}
