package org.example.application.repository.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingFilterBean;

import java.util.UUID;

@Entity
@Table(name="roles")
public class RoleEntity {
    @Id
    @Column(name="id")
    private UUID id;

    @Column(name="code")
    private String code;

    @Column(name="name")
    private String name;

    public RoleEntity() {}

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return this.code.equals("ADMIN");
    }

    public boolean isUser() {
        return this.code.equals("USER");
    }

    public boolean isViewer() {
        return this.code.equals("VIEWER");
    }

    public boolean canCreate() {
        return this.isAdmin();
    }

    public boolean canReadContent() {
        return isAdmin() || isUser();
    }

    public boolean canBrowse() {
        return isAdmin() || isUser() || isViewer();
    }

    public boolean weakerThan(RoleEntity other) {
        if (this.code.equals("VIEWER") && (other.code.equals("USER") || other.code.equals("ADMIN"))) {
            return true;
        }
        if (this.code.equals("USER") && (other.code.equals("ADMIN"))) {
            return true;
        }
        return false;
    }
}
