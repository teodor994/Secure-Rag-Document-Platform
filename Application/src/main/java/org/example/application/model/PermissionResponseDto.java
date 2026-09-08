package org.example.application.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.UUID;

// DTO made for readable format of the nodeAccess table (names instead of ids)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PermissionResponseDto(
        UUID id,
        UUID nodeId,
        String nodePath,
        String targetUserName,
        String targetRoleName,
        String grantedRole,
        boolean inherit,
        String createdBy,
        LocalDateTime createdAt
) {
}
