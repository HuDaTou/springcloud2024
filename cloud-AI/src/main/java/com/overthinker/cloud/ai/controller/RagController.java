package com.overthinker.cloud.ai.controller;

import com.overthinker.cloud.ai.service.RagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * RAG 控制器
 * <p>
 * 提供检索增强生成接口，包括知识库管理和智能问答
 * </p>
 *
 * @author overthinker
 * @since 2024-01-15
 */
@Slf4j
@RestController
@RequestMapping("/api/rag")
@Tag(name = "RAG API", description = "Retrieval-Augmented Generation 检索增强生成接口")
@RequiredArgsConstructor
@Validated
public class RagController {

    private final RagService ragService;

    /**
     * RAG 查询（默认 topK = 3）
     *
     * @param request 包含问题的请求体
     * @return AI 回答
     */
    @Operation(summary = "RAG查询", description = "根据知识库回答问题")
    @PostMapping("/query")
    public Mono<Map<String, String>> query(
            @Parameter(description = "请求体，包含 question 字段", required = true)
            @RequestBody Map<String, String> request
    ) {
        String question = request.get("question");
        log.info("RAG query: {}", question);
        return ragService.query(question)
                .map(answer -> Map.of("answer", answer));
    }

    /**
     * RAG 查询（自定义 topK）
     *
     * @param request 包含问题的请求体
     * @param topK 返回相关文档数量
     * @return AI 回答
     */
    @Operation(summary = "RAG查询(自定义返回数量)", description = "根据知识库回答问题，可指定返回的相关文档数量")
    @PostMapping("/query/{topK}")
    public Mono<Map<String, String>> queryWithTopK(
            @Parameter(description = "请求体，包含 question 字段", required = true)
            @RequestBody Map<String, String> request,
            @Parameter(description = "返回相关文档数量", required = true)
            @PathVariable int topK
    ) {
        String question = request.get("question");
        log.info("RAG query with topK={}: {}", topK, question);
        return ragService.queryWithContext(question, topK)
                .map(answer -> Map.of("answer", answer));
    }

    /**
     * 添加单个文档到知识库
     *
     * @param request 包含文档内容的请求体
     * @return 操作结果
     */
    @Operation(summary = "添加单个文档", description = "向知识库添加单个文档")
    @PostMapping("/documents")
    public Mono<Map<String, String>> addDocument(
            @Parameter(description = "请求体，包含 content 字段", required = true)
            @RequestBody Map<String, String> request
    ) {
        String content = request.get("content");
        log.info("Adding single document");
        return ragService.addDocument(content)
                .then(Mono.just(Map.of("message", "Document added successfully")));
    }

    /**
     * 批量添加文档到知识库
     *
     * @param contents 文档内容列表
     * @return 操作结果
     */
    @Operation(summary = "批量添加文档", description = "向知识库批量添加文档")
    @PostMapping("/documents/batch")
    public Mono<Map<String, String>> addDocuments(
            @Parameter(description = "文档内容列表", required = true)
            @RequestBody @NotEmpty List<String> contents
    ) {
        log.info("Adding {} documents", contents.size());
        return ragService.addDocuments(contents)
                .then(Mono.just(Map.of("message", "Documents added successfully")));
    }

    /**
     * 获取知识库中文档数量
     *
     * @return 文档数量
     */
    @Operation(summary = "获取文档数量", description = "获取知识库中的文档数量")
    @GetMapping("/documents/count")
    public Mono<Map<String, Long>> getDocumentCount() {
        log.info("Getting document count");
        return ragService.getDocumentCount()
                .map(count -> Map.of("count", count));
    }

    /**
     * 清空知识库
     *
     * @return 操作结果
     */
    @Operation(summary = "清空知识库", description = "清空知识库中的所有文档")
    @DeleteMapping("/documents")
    public Mono<Map<String, String>> clearDocuments() {
        log.info("Clearing all documents");
        return ragService.clearAllDocuments()
                .then(Mono.just(Map.of("message", "All documents cleared")));
    }
}
