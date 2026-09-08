package org.example.application.service;

import org.example.application.model.SearchResultDto;
import org.example.application.repository.NodeRepository;
import org.example.application.repository.entity.NodeEntity;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.VectorStoreRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.deser.jdk.UntypedObjectDeserializer;

import java.util.*;

@Service
public class RagService {

    private final VectorStore vectorStore;
    private final NodeRepository nodeRepository;

    @Autowired
    public RagService (VectorStore vectorStore, NodeRepository nodeRepository) {
        this.vectorStore = vectorStore;
        this.nodeRepository = nodeRepository;
    }

    public List<Document> searchRelevantChunks(String userQuery) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(userQuery)                   // text brut
                .topK(4)                            // primele 4 cele mai relevante chunkuri
                .similarityThreshold(0.6)           // prag minim similaritate
                .build();

        return vectorStore.similaritySearch(searchRequest);
    }

    public List<SearchResultDto> searchDocuments(String userQuery) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(userQuery)
                .topK(6)
                .similarityThreshold(0.6)
                .build();

        List<Document> documents = vectorStore.similaritySearch(searchRequest);

        Map<UUID, Document> bestChunkByNode = new LinkedHashMap<>();

        for(Document document : documents) {
            Object nodeIdRaw = document.getMetadata().get("nodeId");
            if(nodeIdRaw == null) {
                continue;
            }
            UUID nodeId = UUID.fromString(nodeIdRaw.toString());
            bestChunkByNode.putIfAbsent(nodeId, document);
        }

        List<SearchResultDto> results = new ArrayList<>();
        for (Map.Entry<UUID, Document> entry : bestChunkByNode.entrySet()) {
            NodeEntity node = nodeRepository.findById(entry.getKey()).orElse(null);
            if (node == null) {
                continue;
            }

            results.add(new SearchResultDto(
                    node.getId(),
                    node.getName(),
                    node.getPath(),
                    buildSnippet(entry.getValue().getText())
            ));
        }

        return results;
    }

    private String buildSnippet(String content) {
        if (content == null) {
            return "";
        }
        int maxLen = 240;
        return content.length() > maxLen ? content.substring(0, maxLen) + "..." : content;
    }


}
