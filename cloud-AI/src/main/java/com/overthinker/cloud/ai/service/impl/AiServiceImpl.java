package com.overthinker.cloud.ai.service.impl;

import com.overthinker.cloud.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * AI 服务实现类
 * <p>
 * 实现 AI 模型切换和对话功能
 * </p>
 *
 * @author overthinker
 * @since 2024-01-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final Map<String, ChatModel> chatModelMap;
    private final ChatClient chatClient;

    /**
     * 切换 AI 模型
     *
     * @param modelId 模型ID
     * @return 切换结果信息
     */
    @Override
    public String changeModel(String modelId) {
        ChatModel chatModel = chatModelMap.get(modelId);
        if (chatModel != null) {
            log.info("Switching chat model to: {}", modelId);
            return "Chat model switched to: " + modelId;
        } else {
            log.warn("Model not found: {}", modelId);
            return "Error: Model not found";
        }
    }

    /**
     * 发送消息给 AI 进行对话
     *
     * @param prompt 用户输入的提示词
     * @return AI 的响应内容
     */
    @Override
    public String chat(String prompt) {
        log.info("Sending chat prompt: {}", prompt);
        return chatClient.prompt().user(prompt).call().content();
    }
}
