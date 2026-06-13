package com.overthinker.cloud.ai.controller;

import com.overthinker.cloud.ai.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * AI 对话控制器
 * <p>
 * 提供 AI 模型切换和对话功能
 * </p>
 *
 * @author overthinker
 * @since 2024-01-15
 */
@Slf4j
@RestController
@RequestMapping("/ai")
@Tag(name = "AI对话相关接口")
@RequiredArgsConstructor
@Validated
public class AiController {

    private final AiService aiService;
    private final ChatClient chatClient;

    /**
     * 切换 AI 模型
     *
     * @param modelId 模型ID
     * @return 切换结果
     */
    @Operation(summary = "切换AI模型")
    @PostMapping("/model/{modelId}")
    public Mono<String> changeModel(
            @Parameter(description = "模型ID", required = true)
            @PathVariable @NotEmpty String modelId
    ) {
        log.info("Changing AI model to: {}", modelId);
        return Mono.fromCallable(() -> aiService.changeModel(modelId))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * AI 对话接口
     *
     * @param prompt 用户输入的提示词
     * @return AI 响应
     */
    @Operation(summary = "AI对话")
    @GetMapping("/chat")
    public Mono<String> chat(
            @Parameter(description = "用户输入", required = true)
            @RequestParam @NotEmpty String prompt
    ) {
        log.info("AI chat request: {}", prompt);
        return Mono.fromCallable(() -> chatClient.prompt()
                        .user(prompt)
                        .call()
                        .content())
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * 询问 AI 身份接口
     *
     * @param message 用户消息
     * @return AI 响应
     */
    @Operation(summary = "询问AI身份")
    @GetMapping("/who")
    public Mono<String> getAiResponse(
            @Parameter(description = "用户消息")
            @RequestParam(value = "message", defaultValue = "你是谁？") String message
    ) {
        log.info("AI who request: {}", message);
        return Mono.fromCallable(() -> chatClient.prompt()
                        .user(message)
                        .call()
                        .content())
                .subscribeOn(Schedulers.boundedElastic());
    }
}
