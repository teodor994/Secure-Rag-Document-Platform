package org.example.application.tools;

import org.example.application.model.SearchResultDto;
import org.example.application.service.RagService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagMcpTools {
    private final RagService ragService;

    public RagMcpTools(RagService ragService) {
        this.ragService = ragService;
    }

    @Tool(description="Search the document store for content matching a natural language query. Returns matching files with name, folder path, and a text snippet.")
    public List<SearchResultDto> searchDocuments(
            @ToolParam(description="Natural language search query") String query
    ) {
        return ragService.searchDocuments(query);
    }
}
