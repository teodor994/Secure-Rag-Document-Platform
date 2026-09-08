package org.example.application.repository;

import org.example.application.repository.entity.NodeContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NodeContentRepository extends JpaRepository<NodeContentEntity, UUID> {
}