package org.example.application.controller;

import org.example.application.model.PermissionResponseDto;
import org.example.application.repository.NodeAccessReadRepository;
import org.example.application.repository.NodeAccessRepository;
import org.example.application.repository.RoleRepository;
import org.example.application.repository.UserRepository;
import org.example.application.repository.entity.NodeAccessEntity;
import org.example.application.repository.entity.NodeAccessReadEntity;
import org.example.application.repository.entity.RoleEntity;
import org.example.application.repository.entity.UserEntity;
import org.example.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Permissions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// Controller that manages all the users interactions with the UI, including permissions
@RestController
@RequestMapping("/api")
public class UserController {
    private UserRepository userRepository;
    private NodeAccessRepository nodeAccessRepository;
    private UserService userService;
    private RoleRepository roleRepository;

    @Autowired
    public UserController(UserRepository userRepository,  NodeAccessRepository nodeAccessRepository,
                          RoleRepository roleRepository,  UserService userService) {
        this.userRepository = userRepository;
        this.nodeAccessRepository = nodeAccessRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    // returns all the current users
    @GetMapping("/users")
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    // Returns all the permissions in the specified readable format
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/permissions")
    public List<PermissionResponseDto> getPermissions() {
        return userService.getAllPermissionsRead();
    }

    // Adds a new permission to the DB
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/permissions")
    public Map<String, Object> addPermissions(@RequestBody NodeAccessEntity permission) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("permission", permission);
        nodeAccessRepository.save(permission);
        return response;
    }

    // Removes the given permission id from the DB
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/permissions/{permissionId}")
    public Map<String, Object> deletePermissions(@PathVariable("permissionId") UUID permissionId) {
        Map<String, Object> response = new HashMap<>();
        try {
            nodeAccessRepository.deleteById(permissionId);
            response.put("success", true);
        }  catch (Exception e) {
            response.put("success", false);
        }
        return response;
    }

    // Return all the roles in the DB
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/roles")
    public List<RoleEntity> getRoles() {
        return roleRepository.findAll();
    }
}


