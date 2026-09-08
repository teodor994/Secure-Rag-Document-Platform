package org.example.application.repository.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;
import java.util.UUID;

// Entity made for Read Only cases, takes all the node_access data into readable string format
@Entity
@Table(name = "node_access")
@Immutable
public class NodeAccessReadEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", insertable = false, updatable = false)
    private NodeEntity node;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "granted_role_id", insertable = false, updatable = false)
    private RoleEntity grantedRole;

    @Column(name = "inherit")
    private boolean inherit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private UserEntity createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UUID getId() { return id; }
    public NodeEntity getNode() { return node; }
    public UserEntity getUser() { return user; }
    public RoleEntity getRole() { return role; }
    public RoleEntity getGrantedRole() { return grantedRole; }
    public boolean isInherit() { return inherit; }
    public UserEntity getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
