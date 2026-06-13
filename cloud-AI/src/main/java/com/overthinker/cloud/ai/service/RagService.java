package com.overthinker.cloud.ai.service;

import reactor.core.publisher.Mono;

import java.util.List;

/**
 * RAG (Retrieval-Augmented Generation) 服务接口
 * <p>
 * 提供检索增强生成相关功能，包括知识库管理和智能问答
 * </p>
 *
 * @author overthinker
 * @since 2024-01-15
 */
public interface RagService {

    /**
     * RAG 查询（使用默认 topK = 3）
     *
     * @param question 用户问题
     * @return AI 回答
     */
    Mono<String> query(String question);

    /**
     * RAG 查询（指定返回文档数量）
     *
     * @param question 用户问题
     * @param topK 返回相关文档数量
     * @return AI 回答
     */
    Mono<String> queryWithContext(String question, int topK);

    /**
     * 添加单个文档到知识库
     *
     * @param content 文档内容
     * @return 空 Mono
     */
    Mono<Void> addDocument(String content);

    /**
     * 批量添加文档到知识库
     *
     * @param contents 文档内容列表
     * @return 空 Mono
     */
    Mono<Void> addDocuments(List<String> contents);

    /**
     * 获取知识库中文档数量
     *
     * @return 文档数量
     */
    Mono<Long> getDocumentCount();

    /**
     * 清空知识库
     *
     * @return 空 Mono
     */
    Mono<Void> clearAllDocuments();
}
