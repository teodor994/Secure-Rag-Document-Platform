package org.example.application.repository.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "oidc_group_role_mappings")
public class OidcGroupRoleEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "oidc_group", nullable = false)
    private String oidcGroup;

    // de verificat asta cu eager pe viitor poate fi problematica pentru cazuri mai complexe
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    public OidcGroupRoleEntity() {
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getOidcGroup() {
        return oidcGroup;
    }
    public void setOidcGroup(String oidcGroup) {
        this.oidcGroup = oidcGroup;
    }
    public RoleEntity getRole() {
        return role;
    }
    public void setRole(RoleEntity role) {
        this.role = role;
    }
}