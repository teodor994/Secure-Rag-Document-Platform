package org.example.application.controller;

import org.example.application.config.CustomUserDetails;
import org.example.application.repository.NodeRepository;
import org.example.application.repository.entity.NodeEntity;
import org.example.application.repository.entity.RoleEntity;
import org.example.application.service.DocumentsService;
import org.example.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

// Controller that manages getting data about the nodes
@RestController
@RequestMapping("/api/nodes")
public class DocumentsController {
    private final DocumentsService documentsService;
    private final NodeRepository nodeRepository;
    private final UserService userService;

    @Autowired
    public DocumentsController(DocumentsService documentsService,  NodeRepository nodeRepository,
                               UserService userService) {
        this.documentsService = documentsService;
        this.nodeRepository = nodeRepository;
        this.userService = userService;
    }

    // Returns children nodes of nodeId
    @GetMapping("/{nodeId}/children")
    public ResponseEntity<Page<NodeEntity>> getDirectChildren(
            @PathVariable UUID nodeId,
            @PageableDefault(sort = "name") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails authentication) {
        List<NodeEntity> children = documentsService.getChildren(nodeId, pageable);

        List<NodeEntity> allowedChildren = children.stream()
                .filter(node -> {
                    RoleEntity role = userService.getPermission(authentication, node);
                    return (role != null && role.canBrowse()) || authentication.getRole().equals("ADMIN");
                })
                .toList();

        // indicii pentru pagina curenta
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allowedChildren.size());

        List<NodeEntity> pageContent = (start <= allowedChildren.size())
                ? allowedChildren.subList(start, end)
                : Collections.emptyList();

        Page<NodeEntity> page = new PageImpl<>(pageContent, pageable, allowedChildren.size());

        return ResponseEntity.ok(page);
    }

    // Return children nodes of the root, which has null id
    @GetMapping("/root")
    public ResponseEntity<Page<NodeEntity>> getRootNodes(
            @PageableDefault(sort = "name") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails authentication
    ) {
        List<NodeEntity> roots = documentsService.getRootNodes(pageable);
        List<NodeEntity> allowedRoots = roots.stream()
                .filter(node -> {
                    RoleEntity role = userService.getPermission(authentication, node);
                    return (role != null && role.canBrowse()) || authentication.getRole().equals("ADMIN");
                })
                .toList();
        //List<NodeEntity> allowedRoots = roots;

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allowedRoots.size());

        List<NodeEntity> pageContent = (start <= allowedRoots.size())
                ? allowedRoots.subList(start, end)
                : Collections.emptyList();

        Page<NodeEntity> page = new PageImpl<>(pageContent, pageable, allowedRoots.size());
        return ResponseEntity.ok(page);
    }

    // Function used on refresh, return all the children of the active folder in the path
    @GetMapping("/resolve-path")
    public ResponseEntity<List<NodeEntity>> resolvePath(@RequestParam String path) {
        List<String> segments = Arrays.stream(path.split("/"))
                .filter(s -> !s.isBlank())
                .toList();

        if (segments.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        List<NodeEntity> resolved = documentsService.resolvePath(segments);

        return ResponseEntity.ok(resolved);
    }

    // Deletes documentId doc from the DB
    @DeleteMapping("/{documentId}")
    public Map<String, Object> deleteNode(@PathVariable UUID documentId,
                                          @AuthenticationPrincipal CustomUserDetails authentication) {
        Map<String, Object> response = new HashMap<>();
        if (!userService.getPermission(authentication, documentId).canCreate()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        try {
            nodeRepository.deleteById(documentId);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
       return response;
    }
}
