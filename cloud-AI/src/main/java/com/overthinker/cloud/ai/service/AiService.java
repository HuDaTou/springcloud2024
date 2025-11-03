package com.overthinker.cloud.ai.service;

public interface AiService {

    String changeModel(String id);

    String chat(String prompt);
}
