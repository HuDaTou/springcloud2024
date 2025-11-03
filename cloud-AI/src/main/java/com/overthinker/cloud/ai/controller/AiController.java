package com.overthinker.cloud.ai.controller;

import com.overthinker.cloud.ai.service.impl.AiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiServiceImpl aiService;

    @PostMapping("/model/{modelId}")
    public String changeModel(@PathVariable String modelId) {
        return aiService.changeModel(modelId);
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        return aiService.chat(prompt);
    }
}
