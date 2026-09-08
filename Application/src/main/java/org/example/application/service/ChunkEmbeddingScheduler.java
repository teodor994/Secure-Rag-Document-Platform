package org.example.application.service;

import org.example.application.model.ProcessingStatus;
import org.example.application.repository.NodeContentRepository;
import org.example.application.repository.NodeRepository;
import org.example.application.repository.entity.NodeContentEntity;
import org.example.application.repository.entity.NodeEntity;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ChunkEmbeddingScheduler {
    private final NodeRepository nodeRepository;
    private final NodeContentRepository nodeContentRepository;
    private final VectorStore vectorStore;

    public ChunkEmbeddingScheduler(NodeRepository nodeRepository,
                                   NodeContentRepository nodeContentRepository,
                                   VectorStore vectorStore) {
        this.nodeRepository = nodeRepository;
        this.nodeContentRepository = nodeContentRepository;
        this.vectorStore = vectorStore;
    }

    @Scheduled(fixedDelay = 60000)
    public void processPendingDocuments() {
        System.out.println("Processing pending documents");
        List<NodeEntity> pendingNodes = nodeRepository.findByProcessingStatus(ProcessingStatus.PENDING);

        for (NodeEntity node : pendingNodes) {
            try {
                // Selected node is marked for processing
                node.setProcessingStatus(ProcessingStatus.PROCESSING);
                nodeRepository.save(node);

                NodeContentEntity content = nodeContentRepository.findById(node.getId()).orElse(null);
                if (content != null &&
                    content.getTextContent() != null &&
                    !content.getTextContent().isBlank()) {
                    Map<String, Object> metadata = Map.of(
                            "nodeId", node.getId().toString()
                    );
                    Document rawDocument = new Document(content.getTextContent(), metadata);
                    TokenTextSplitter splitter = TokenTextSplitter.builder()
                            .withChunkSize(700)
                            .withMinChunkSizeChars(200)
                            .build();
                    // Chunk split for embeddings
                    List<Document> chunks = splitter.apply(List.of(rawDocument));
                    // Send the chunks to the Embedding Model and saves it in DB
                    vectorStore.accept(chunks);
                    node.setProcessingStatus(ProcessingStatus.DONE);
                } else {
                    System.out.println("Document not found");
                    node.setProcessingStatus(ProcessingStatus.NO_CONTENT);
                }
                nodeRepository.save(node);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                node.setProcessingStatus(ProcessingStatus.FAILED);
                nodeRepository.save(node);
            }
        }
    }
}
