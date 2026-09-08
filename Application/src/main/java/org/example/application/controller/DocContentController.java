package org.example.application.controller;


import org.example.application.config.CustomUserDetails;
import org.example.application.repository.NodeContentRepository;
import org.example.application.repository.entity.NodeContentEntity;
import org.example.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

// Controller that manages document contents
@RestController
@RequestMapping("/api")
public class DocContentController {
    private final NodeContentRepository nodeContentRepository;
    private final UserService userService;

    @Autowired
    public DocContentController(NodeContentRepository nodeContentRepository,  UserService userService) {
        this.nodeContentRepository = nodeContentRepository;
        this.userService = userService;
    }

    @GetMapping("/nodes/{docId}/content")
    public NodeContentEntity getDocContent(@PathVariable UUID docId,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (userService.getPermission(customUserDetails, docId) == null ||
                !userService.getPermission(customUserDetails, docId).canReadContent()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        try {
            return nodeContentRepository.getById(docId);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
