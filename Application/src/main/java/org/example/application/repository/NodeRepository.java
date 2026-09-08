package org.example.application.repository;

import org.example.application.model.ProcessingStatus;
import org.example.application.repository.entity.NodeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NodeRepository extends JpaRepository<NodeEntity, UUID> {
    //sa iau copiii unui nod
    Page<NodeEntity> findByParentId(UUID parentId, Pageable pageable);

    List<NodeEntity> findByParentId(UUID parentId);

    //nodurile root
    Page<NodeEntity> findByParentIdIsNull(Pageable pageable);

    List<NodeEntity> findByParentIdIsNull();

    boolean existsByParentIdAndNameAndType(UUID parentId, String name, String type);
    // pentru rooturi
    boolean existsByParentIdIsNullAndNameAndType(String name, String type);

    List<NodeEntity> findByProcessingStatus(ProcessingStatus processingStatus);

}
