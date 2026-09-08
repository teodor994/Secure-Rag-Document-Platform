package org.example.application.repository;

import org.example.application.repository.entity.OidcGroupRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface OidcGroupRoleRepository
        extends JpaRepository<OidcGroupRoleEntity, UUID> {

    List<OidcGroupRoleEntity> findByOidcGroupIn(Collection<String> groups);
}