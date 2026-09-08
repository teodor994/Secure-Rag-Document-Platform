package org.example.application.repository;

import org.example.application.repository.entity.NodeAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface NodeAccessRepository extends JpaRepository<NodeAccessEntity, UUID> {
}
