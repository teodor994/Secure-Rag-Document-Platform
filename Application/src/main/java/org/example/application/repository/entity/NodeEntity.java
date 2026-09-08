package org.example.application.repository.entity;

import jakarta.persistence.*;
import org.example.application.model.ProcessingStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="nodes")
public class NodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id", nullable=true)
    private UUID id;

    @Column(name="parent_id")
    private UUID parentId;

    @Column(name="type", nullable=false)
    private String type;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="created_by")
    private UUID createdBy;

    @CreationTimestamp
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @Column(name="path", nullable=false)
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status")
    private ProcessingStatus processingStatus;

    public UUID getId() {return id;}
    public void setId(UUID id) {this.id = id;}
    public UUID getParentId() {return parentId;}
    public void setParentId(UUID parentId) {this.parentId = parentId;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public UUID getCreatedBy() {return createdBy;}
    public void setCreatedBy(UUID createdBy) {this.createdBy = createdBy;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
    public String getPath() {return path;}
    public void setPath(String path) {this.path = path;}
    public ProcessingStatus getProcessingStatus() {return processingStatus;}
    public void setProcessingStatus(ProcessingStatus processingStatus) {this.processingStatus = processingStatus;}
}
