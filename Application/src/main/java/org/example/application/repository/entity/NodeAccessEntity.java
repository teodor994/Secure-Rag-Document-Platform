package org.example.application.repository.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="node_access")
public class NodeAccessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "node_id")
    private UUID nodeId;

    @Nullable
    @Column(name = "user_id")
    private UUID userId;

    @Nullable
    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "granted_role_id")
    private UUID grantedRoleId;

    @Column(name = "inherit")
    private boolean inherit;

    @Column(name = "created_by")
    private UUID createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getNodeId() {
        return nodeId;
    }
    public void setNodeId(UUID nodeId) {
        this.nodeId = nodeId;
    }
    public @Nullable UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public @Nullable UUID getRoleId() {
        return roleId;
    }
    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }
    public UUID getGrantedRoleId() {
        return grantedRoleId;
    }
    public void setGrantedRoleId(UUID grantedRoleId) {
        this.grantedRoleId = grantedRoleId;
    }
    public boolean isInherit() {
        return inherit;
    }
    public void setInherit(boolean inherit) {
        this.inherit = inherit;
    }
    public UUID getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
