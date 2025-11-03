package com.overthinker.cloud.ai.controller;

import com.overthinker.cloud.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers; // 导入 Schedulers

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatAPI {

    final ChatClient chatClient;
    final AiService aiService;

    /**
     * 处理 AI 聊天请求，将潜在的阻塞调用调度到弹性线程池。
     * @param message 用户的输入消息
     * @return 包含 AI 响应内容的 Mono
     */
    @GetMapping("/who")
    public Mono<String> getAiResponse(@RequestParam(value = "message", defaultValue = "你是谁？") String message) {
        return Mono.fromCallable(() -> {
                    // chatClient.call() 如果底层是同步实现，此处将是阻塞点
                    return chatClient.prompt()
                            .user(message)
                            .call()
                            .content();
                })
                // 关键修复: 将阻塞操作切换到 Schedulers.boundedElastic 线程池
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * 更改 AI 模型，并将潜在的阻塞调用调度到弹性线程池。
     * @param id 模型 ID
     * @return 更改模型操作的结果
     */
    @GetMapping("/change/{id}")
    public Mono<String> changeModel(@PathVariable("id") String id) {
        // 使用 Mono.fromCallable 来包裹 aiService 中的潜在阻塞调用
        return Mono.fromCallable(() -> {
                    // aiService.changeModel() 如果包含 I/O 或耗时操作，此处将是阻塞点
                    return aiService.changeModel(id);
                })
                // 关键修复: 将阻塞操作切换到 Schedulers.boundedElastic 线程池
                .subscribeOn(Schedulers.boundedElastic());
    }

}
