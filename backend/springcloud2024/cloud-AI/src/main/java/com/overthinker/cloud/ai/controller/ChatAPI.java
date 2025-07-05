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


//
//    final VectorStore vectorStore;
//
//    @GetMapping("/ai/chat")
//    public String chat(@RequestParam(value = "message") String message) {
//
//        return chatClient.prompt("你还是一个dj").user(message).call().content();
//    }
//
//    @GetMapping("/ai/chat2")
//    public void chat2() {
////        // 要检查和创建的集合名称
////        String collectionName = "vector_store";
////
////        HasCollectionParam build = HasCollectionParam.newBuilder().withCollectionName(collectionName).build();
////        String message = milvusClient.hasCollection(build).getMessage();
////        System.out.printf(message);
//
//
//        List<Document> documents = List.of(
//                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
//                new Document("The World is Big and Salvation Lurks Around the Corner"),
//                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));
//
//// Add the documents to Milvus Vector Store
//        vectorStore.add(documents);
////        vectorStore.write(documents);
//// Retrieve documents similar to a query
//        List<Document> results = this.vectorStore.similaritySearch(SearchRequest.builder().query("Spring").topK(5).build());
//
//    }

    @GetMapping("/ai/weather")
    public String getWeather(@RequestParam String message) {
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
