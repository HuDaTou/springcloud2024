

import reactor.core.publisher.Mono;

public interface RagService {

    Mono<String> query(String question);

    Mono<String> queryWithContext(String question, int topK);

    Mono<Void> addDocument(String content);

    Mono<Void> addDocuments(java.util.List<String> contents);

    Mono<Long> getDocumentCount();

    Mono<Void> clearAllDocuments();
}