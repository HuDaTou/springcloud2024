package com.overthinker.cloud.ai.service.impl;

import com.overthinker.cloud.ai.service.RagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RagServiceImpl implements RagService {

    private static final Logger logger = LoggerFactory.getLogger(RagServiceImpl.class);

    private final ChatClient chatClient;
    private final Map<String, String> documentStore = new ConcurrentHashMap<>();

    public RagServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Mono<String> query(String question) {
        return queryWithContext(question, 3);
    }

    @Override
    public Mono<String> queryWithContext(String question, int topK) {
        return Mono.fromCallable(() -> {
                    List<String> relevantDocs = findRelevantDocuments(question, topK);
                    
                    if (relevantDocs.isEmpty()) {
                        logger.info("No documents found for query: {}", question);
                        return chatClient.prompt()
                                .user(question)
                                .call()
                                .content();
                    }

                    String context = String.join("\n\n", relevantDocs);
                    logger.info("Found {} relevant documents for query", relevantDocs.size());

                    String systemPrompt = "你是一个智能助手，擅长根据提供的参考文档回答问题。请使用以下上下文信息来回答用户的问题：\n\n" + context;
                    return chatClient.prompt()
                            .system(systemPrompt)
                            .user(question)
                            .call()
                            .content();
                })
                .onErrorResume(e -> {
                    logger.error("Error during RAG query", e);
                    return Mono.just("查询过程中发生错误: " + e.getMessage());
                });
    }

    private List<String> findRelevantDocuments(String query, int topK) {
        return documentStore.values().stream()
                .filter(doc -> containsAnyKeyword(doc, query))
                .limit(topK)
                .collect(Collectors.toList());
    }

    private boolean containsAnyKeyword(String document, String query) {
        String[] queryWords = query.toLowerCase().split("\\s+");
        String docLower = document.toLowerCase();
        for (String word : queryWords) {
            if (docLower.contains(word)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Mono<Void> addDocument(String content) {
        return Mono.fromRunnable(() -> {
            String id = UUID.randomUUID().toString();
            documentStore.put(id, content);
            logger.info("Added document with id: {}, length: {} characters", id, content.length());
        });
    }

    @Override
    public Mono<Void> addDocuments(List<String> contents) {
        return Mono.fromRunnable(() -> {
            for (String content : contents) {
                String id = UUID.randomUUID().toString();
                documentStore.put(id, content);
            }
            logger.info("Added {} documents", contents.size());
        });
    }

    @Override
    public Mono<Long> getDocumentCount() {
        return Mono.just((long) documentStore.size());
    }

    @Override
    public Mono<Void> clearAllDocuments() {
        return Mono.fromRunnable(() -> {
            documentStore.clear();
            logger.info("Cleared all documents from store");
        });
    }
}