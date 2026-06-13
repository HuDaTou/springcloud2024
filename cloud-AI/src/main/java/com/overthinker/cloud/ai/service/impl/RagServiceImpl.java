package com.overthinker.cloud.ai.service.impl;

import com.overthinker.cloud.ai.service.RagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * RAG (Retrieval-Augmented Generation) 服务实现类
 * <p>
 * 实现检索增强生成相关功能
 * </p>
 *
 * @author overthinker
 * @since 2024-01-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RagServiceImpl implements RagService {

    private final ChatClient chatClient;
    private final Map<String, String> documentStore = new ConcurrentHashMap<>();

    /**
     * RAG 查询（使用默认 topK = 3）
     *
     * @param question 用户问题
     * @return AI 回答
     */
    @Override
    public Mono<String> query(String question) {
        return queryWithContext(question, 3);
    }

    /**
     * RAG 查询（指定返回文档数量）
     *
     * @param question 用户问题
     * @param topK 返回相关文档数量
     * @return AI 回答
     */
    @Override
    public Mono<String> queryWithContext(String question, int topK) {
        return Mono.fromCallable(() -> {
                    List<String> relevantDocs = findRelevantDocuments(question, topK);

                    if (relevantDocs.isEmpty()) {
                        log.info("No documents found for query: {}", question);
                        return chatClient.prompt()
                                .user(question)
                                .call()
                                .content();
                    }

                    String context = String.join("\n\n", relevantDocs);
                    log.info("Found {} relevant documents for query", relevantDocs.size());

                    String systemPrompt = "你是一个智能助手，擅长根据提供的参考文档回答问题。请使用以下上下文信息来回答用户的问题：\n\n" + context;
                    return chatClient.prompt()
                            .system(systemPrompt)
                            .user(question)
                            .call()
                            .content();
                })
                .onErrorResume(e -> {
                    log.error("Error during RAG query", e);
                    return Mono.just("查询过程中发生错误: " + e.getMessage());
                });
    }

    /**
     * 查找相关文档
     *
     * @param query 查询词
     * @param topK 返回数量
     * @return 相关文档列表
     */
    private List<String> findRelevantDocuments(String query, int topK) {
        return documentStore.values().stream()
                .filter(doc -> containsAnyKeyword(doc, query))
                .limit(topK)
                .collect(Collectors.toList());
    }

    /**
     * 检查文档是否包含查询词中的任意关键词
     *
     * @param document 文档内容
     * @param query 查询词
     * @return 是否匹配
     */
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

    /**
     * 添加单个文档到知识库
     *
     * @param content 文档内容
     * @return 空 Mono
     */
    @Override
    public Mono<Void> addDocument(String content) {
        return Mono.fromRunnable(() -> {
            String id = UUID.randomUUID().toString();
            documentStore.put(id, content);
            log.info("Added document with id: {}, length: {} characters", id, content.length());
        });
    }

    /**
     * 批量添加文档到知识库
     *
     * @param contents 文档内容列表
     * @return 空 Mono
     */
    @Override
    public Mono<Void> addDocuments(List<String> contents) {
        return Mono.fromRunnable(() -> {
            for (String content : contents) {
                String id = UUID.randomUUID().toString();
                documentStore.put(id, content);
            }
            log.info("Added {} documents", contents.size());
        });
    }

    /**
     * 获取知识库中文档数量
     *
     * @return 文档数量
     */
    @Override
    public Mono<Long> getDocumentCount() {
        return Mono.just((long) documentStore.size());
    }

    /**
     * 清空知识库
     *
     * @return 空 Mono
     */
    @Override
    public Mono<Void> clearAllDocuments() {
        return Mono.fromRunnable(() -> {
            documentStore.clear();
            log.info("Cleared all documents from store");
        });
    }
}
