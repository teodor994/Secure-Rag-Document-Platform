package org.example.application.service;

import org.example.application.repository.OidcGroupRoleRepository;
import org.example.application.repository.entity.OidcGroupRoleEntity;
import org.example.application.repository.entity.RoleEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class RoleService {

    public static final List<String> ROLE_PRIORITY = List.of(
            "ADMIN",
            "USER",
            "VIEWER"
    );

    private final OidcGroupRoleRepository oidcGroupRoleRepository;

    public RoleService(OidcGroupRoleRepository oidcGroupRoleRepository) {
        this.oidcGroupRoleRepository = oidcGroupRoleRepository;
    }

    public RoleEntity getStrongestRole(Collection<String> groups) {

        List<OidcGroupRoleEntity> mappings = oidcGroupRoleRepository.findByOidcGroupIn(groups);

        return ROLE_PRIORITY.stream()
                .map(priorityCode -> mappings.stream()
                        .map(OidcGroupRoleEntity::getRole)
                        .filter(role -> priorityCode.equals(role.getCode()))
                        .findFirst()
                        .orElse(null))
                .filter(role -> role != null)
                .findFirst()
                .orElse(null);
    }
}