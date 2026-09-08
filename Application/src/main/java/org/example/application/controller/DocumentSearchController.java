package org.example.application.controller;

import org.example.application.model.SearchResultDto;
import org.example.application.service.RagService;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nodes/search")
public class DocumentSearchController {
    private final RagService ragService;

    public DocumentSearchController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping
    public List<Document> getDocuments(@RequestBody String userQuery)
    {
        return ragService.searchRelevantChunks(userQuery);
    }

    @PostMapping("/files")
    public List<SearchResultDto> getDocumentFiles(@RequestBody String userQuery)
    {
        return ragService.searchDocuments(userQuery);
    }
}
