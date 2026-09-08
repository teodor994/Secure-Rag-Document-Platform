package org.example.application.config;


import org.example.application.service.RagService;
import org.example.application.tools.RagMcpTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpToolsConfig {

    @Bean
    public ToolCallbackProvider ragToolCallbackProvider(RagMcpTools ragMcpTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(ragMcpTools)
                .build();
    }
}
