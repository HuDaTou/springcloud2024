package com.overthinker.cloud.ai.controller;


import io.milvus.client.MilvusServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ChatAPI {

    final ChatClient chatClient;

    final MilvusServiceClient milvusClient;

    final VectorStore vectorStore;

    @GetMapping("/ai/chat")
    public String chat(@RequestParam(value = "message") String message) {

        return chatClient.prompt("你还是一个dj").user(message).call().content();
    }

    @GetMapping("/ai/chat2")
    public void chat2() {
//        // 要检查和创建的集合名称
//        String collectionName = "vector_store";
//
//        HasCollectionParam build = HasCollectionParam.newBuilder().withCollectionName(collectionName).build();
//        String message = milvusClient.hasCollection(build).getMessage();
//        System.out.printf(message);


        List<Document> documents = List.of(
                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));

// Add the documents to Milvus Vector Store
        vectorStore.add(documents);
//        vectorStore.write(documents);
// Retrieve documents similar to a query
        List<Document> results = this.vectorStore.similaritySearch(SearchRequest.builder().query("Spring").topK(5).build());

    }
}
