package org.example.application.model;

import java.util.UUID;

public record SearchResultDto(
        UUID nodeId,
        String name,
        String path,
        String snippet
) {}
