package org.example.application.service;

import org.example.application.config.CustomUserDetails;
import org.example.application.model.PermissionResponseDto;
import org.example.application.repository.NodeAccessReadRepository;
import org.example.application.repository.NodeRepository;
import org.example.application.repository.entity.NodeAccessReadEntity;
import org.example.application.repository.entity.NodeEntity;
import org.example.application.repository.entity.RoleEntity;
import org.example.application.repository.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.*;

import static org.example.application.service.RoleService.ROLE_PRIORITY;

@Service
public class UserService {
    private final NodeAccessReadRepository nodeAccessReadRepository;
    private final NodeRepository nodeRepository;
    private final RoleService roleService;

    @Autowired
    public UserService(NodeAccessReadRepository nodeAccessReadRepository, NodeRepository nodeRepository, RoleService roleService) {
        this.nodeAccessReadRepository = nodeAccessReadRepository;
        this.nodeRepository = nodeRepository;
        this.roleService = roleService;
    }

    // Creates DTO list of permisions from the DB query result
    public List<PermissionResponseDto> getAllPermissionsRead() {
        return nodeAccessReadRepository.findAllPermissionsWithDetails().stream().map(entity -> {

            String targetUserName = entity.getUser() != null ? entity.getUser().getEmail() : null;
            String targetRoleName = entity.getRole() != null ? entity.getRole().getName() : null;
            String grantedRoleName = entity.getGrantedRole().getName();
            String createdByName = entity.getCreatedBy().getEmail();

            return new PermissionResponseDto(
                    entity.getId(),
                    entity.getNode().getId(),
                    entity.getNode().getPath(),
                    targetUserName,
                    targetRoleName,
                    grantedRoleName,
                    entity.isInherit(),
                    createdByName,
                    entity.getCreatedAt()
            );
        }).toList();
    }

    public RoleEntity getPermission(CustomUserDetails authentication, NodeEntity node) {
        if (node == null) {
            return null;
        }

        String path = node.getPath();
        if (path == null || path.trim().isEmpty() || path.equals("/")) {
            return null;
        }

        String[] folders = path.split("/");
        RoleEntity inheritedRole = null;

        UUID currUserUuid = UUID.fromString(authentication.getUsername());
        UUID currUserRole = UUID.fromString(authentication.getRoleId());

        List<RoleEntity> currFolderRoleAcc = new ArrayList<>();

        for (String folder : folders) {
            if (folder.isEmpty()) {
                continue;
            }

            List<NodeAccessReadEntity> currFolderDetails =
                    nodeAccessReadRepository.findAllPermissionsByUserRoleId(currUserUuid, currUserRole, folder);

            // Separam permisiunile specifice de User de cele specifice de Rol
            List<RoleEntity> userRoles = new ArrayList<>();
            List<RoleEntity> roleRoles = new ArrayList<>();

            for (NodeAccessReadEntity detail : currFolderDetails) {
                if (detail != null && detail.getGrantedRole() != null) {
                    if (detail.getUser() != null && currUserUuid.equals(detail.getUser().getId())) {
                        userRoles.add(detail.getGrantedRole());
                    } else {
                        roleRoles.add(detail.getGrantedRole());
                    }
                }
            }

            // 1. search for the most important role
            RoleEntity strongestRole = null;
            for (String priorityCode : roleService.ROLE_PRIORITY) {
                for (RoleEntity role : userRoles) {
                    if (role != null && priorityCode.equals(role.getCode())) {
                        strongestRole = role;
                        break;
                    }
                }
                if (strongestRole != null) {
                    break;
                }
            }

            // 2. if it s not found on the user, we search for the role on ROLE
            if (strongestRole == null) {
                for (String priorityCode : roleService.ROLE_PRIORITY) {
                    for (RoleEntity role : roleRoles) {
                        if (role != null && priorityCode.equals(role.getCode())) {
                            strongestRole = role;
                            break;
                        }
                    }
                    if (strongestRole != null) {
                        break;
                    }
                }
            }

            // Actualizăm permisiunea moștenită (inheritedRole) dacă există flag-ul inherit
            for (NodeAccessReadEntity detail : currFolderDetails) {
                if (detail != null && detail.isInherit() && detail.getGrantedRole() != null) {
                    if (inheritedRole == null || inheritedRole.weakerThan(detail.getGrantedRole())) {
                        inheritedRole = detail.getGrantedRole();
                        System.out.println("Am mostenit " + inheritedRole.getCode());
                    }
                }
            }

            // Stabilim permisiunea efectivă pentru folderul curent
            if (strongestRole != null) {
                currFolderRoleAcc.add(strongestRole);
            } else if (inheritedRole != null) {
                currFolderRoleAcc.add(inheritedRole);
            }
        }

        return currFolderRoleAcc.isEmpty() ? null : currFolderRoleAcc.getLast();
    }

    public RoleEntity getPermission(CustomUserDetails authentication, UUID nodeId) {
        NodeEntity node = nodeRepository.findById(nodeId).orElse(null);
        if (node != null) {
            return this.getPermission(authentication, node);
        } else {
            return null;
        }
    }
}
