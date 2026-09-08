package org.example.application.controller;

import org.example.application.config.CustomUserDetails;
import org.example.application.repository.NodeRepository;
import org.example.application.repository.entity.NodeEntity;
import org.example.application.service.DocumentsService;
import org.example.application.service.UserService;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Controller that manages adding files/folders and removing them
@RestController
@RequestMapping("/api")
public class DocumentsAddFilesController {
    private final NodeRepository nodeRepository;
    private final DocumentsService documentsService;
    private final UserService userService;

    @Autowired
    public DocumentsAddFilesController(NodeRepository nodeRepository,  DocumentsService documentsService,
                                        UserService userService) {
        this.nodeRepository = nodeRepository;
        this.documentsService = documentsService;
        this.userService = userService;
    }

    @PostMapping("/nodes")
    public ResponseEntity<Map<String, Object>> addNode(@RequestBody NodeEntity nodeEntity,
                                                       @AuthenticationPrincipal CustomUserDetails authentication) {
        if (userService.getPermission(authentication, nodeEntity.getParentId()) == null ||
                !userService.getPermission(authentication, nodeEntity.getParentId()).canCreate()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Map<String, Object> result = documentsService.createFolder(nodeEntity);
        boolean success = Boolean.TRUE.equals(result.get("success"));

        return success
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    @PostMapping(value = "/nodes/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadNode(
            @RequestParam("parentId") String parentId,
            @RequestParam("path") String path,
            @RequestParam("file") MultipartFile file,
            @RequestParam("createdBy") String createdBy,
            @AuthenticationPrincipal CustomUserDetails authentication) throws IOException {
        if (userService.getPermission(authentication, UUID.fromString(parentId)) == null ||
                !userService.getPermission(authentication, UUID.fromString(parentId)).canCreate()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }        
        Map<String, Object> result = documentsService.uploadFile(file, path, parentId, createdBy);
        boolean success = Boolean.TRUE.equals(result.get("success"));
        return success
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }
}
