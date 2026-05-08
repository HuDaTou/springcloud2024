package com.overthinker.cloud.ai.controller;

import com.overthinker.cloud.ai.service.RagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rag")
@Tag(name = "RAG API", description = "Retrieval-Augmented Generation 检索增强生成接口")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/query")
    @Operation(summary = "RAG查询", description = "根据知识库回答问题")
    public Mono<ResponseEntity<Map<String, String>>> query(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        return ragService.query(question)
                .map(answer -> ResponseEntity.ok(Map.of("answer", answer)));
    }

    @PostMapping("/query/{topK}")
    @Operation(summary = "RAG查询(自定义返回数量)", description = "根据知识库回答问题，可指定返回的相关文档数量")
    public Mono<ResponseEntity<Map<String, String>>> queryWithTopK(
            @RequestBody Map<String, String> request,
            @PathVariable int topK) {
        String question = request.get("question");
        return ragService.queryWithContext(question, topK)
                .map(answer -> ResponseEntity.ok(Map.of("answer", answer)));
    }

    @PostMapping("/documents")
    @Operation(summary = "添加单个文档", description = "向知识库添加单个文档")
    public Mono<ResponseEntity<Map<String, String>>> addDocument(@RequestBody Map<String, String> request) {
        String content = request.get("content");
        return ragService.addDocument(content)
                .then(Mono.just(ResponseEntity.ok(Map.of("message", "Document added successfully"))));
    }

    @PostMapping("/documents/batch")
    @Operation(summary = "批量添加文档", description = "向知识库批量添加文档")
    public Mono<ResponseEntity<Map<String, String>>> addDocuments(@RequestBody List<String> contents) {
        return ragService.addDocuments(contents)
                .then(Mono.just(ResponseEntity.ok(Map.of("message", "Documents added successfully"))));
    }

    @GetMapping("/documents/count")
    @Operation(summary = "获取文档数量", description = "获取知识库中的文档数量")
    public Mono<ResponseEntity<Map<String, Long>>> getDocumentCount() {
        return ragService.getDocumentCount()
                .map(count -> ResponseEntity.ok(Map.of("count", count)));
    }

    @DeleteMapping("/documents")
    @Operation(summary = "清空知识库", description = "清空知识库中的所有文档")
    public Mono<ResponseEntity<Map<String, String>>> clearDocuments() {
        return ragService.clearAllDocuments()
                .then(Mono.just(ResponseEntity.ok(Map.of("message", "All documents cleared"))));
    }
}