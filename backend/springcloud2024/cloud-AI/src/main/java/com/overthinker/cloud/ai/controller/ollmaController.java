package com.overthinker.cloud.ai.controller;

import com.overthinker.cloud.controller.base.BaseController;
import com.overthinker.cloud.resp.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.ListModelResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ollama")

public class ollmaController extends BaseController {

    private final OllamaApi ollamaApi;
    private final ChatClient ollamaChatclient;

    @GetMapping("/list")
    public ResultData<ListModelResponse> ollmaList() {
        return messageHandler(ollamaApi::listModels);
    }


    @GetMapping("/choosemodel")
    public ResultData<ChatClient.CallResponseSpec> ollmaChat(@RequestParam("model") String model, @RequestParam("message") String message) {


        return messageHandler(() -> ollamaChatclient.prompt().user(message).call());



    }

}
