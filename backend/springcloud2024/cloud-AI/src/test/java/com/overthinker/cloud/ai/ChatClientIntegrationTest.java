//package com.overthinker.cloud.ai;
//
//
//import org.junit.Test;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.ollama.OllamaContainer;
//
///**
// * 使用 Testcontainers 的集成测试，用于验证与 Ollama 模型的端到端连接。
// */
//@Testcontainers
//@SpringBootTest
//public class ChatClientIntegrationTest {
//
//    // 定义一个静态的 Ollama 容器实例。
//    // 使用一个轻量级的模型以加快测试速度。
//    @Container
//    private static final OllamaContainer ollama = new OllamaContainer("ollama/ollama:0.1.48");
//
//    /**
//     * 动态地将 Spring AI 的配置指向由 Testcontainers 启动的 Ollama 容器。
//     * 这会覆盖 application.properties 中可能存在的任何相关配置。
//     *
//     * @param registry 动态属性注册表
//     */
//    @DynamicPropertySource
//    static void registerOllamaProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.ai.ollama.base-url", ollama::getEndpoint);
//        registry.add("spring.ai.ollama.chat.options.model", () -> "tinyllama");
//    }
//
//    @Autowired
//    private ChatClient chatClient;
//
//    @Test
//    void chatClientShouldRespond() {
//        // given
//        String message = "What is the capital of France?";
//
//        // when
//        String response = chatClient.prompt()
//                .user(message)
//                .call()
//                .content();
//
//        // then
//        assertThat(response).isNotNull();
//        assertThat(response.toLowerCase()).contains("paris");
//
//        System.out.println("--- Ollama Response ---");
//        System.out.println(response);
//        System.out.println("-----------------------");
//    }
//}
