package org.example.application.config;

import org.example.application.repository.entity.RoleEntity;
import org.example.application.service.RoleService;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// Metadata that will be linked with the session id
public class CustomUserDetails implements UserDetails, Serializable {
    private static final long serialVersionUID = 1L;
    private UUID uuid;
    private String email;
    private String password;
    private final List<String> groups;
    private final String roleCode;
    private final String roleId;

    public CustomUserDetails(UUID uuid, String email, String password, List<String> groups, String roleCode, String roleId) {
        this.uuid = uuid;
        this.email = email;
        this.password = password;
        this.groups = groups;
        this.roleCode = roleCode;
        this.roleId = roleId;
    }

    // TO EDIT GETTING THE ROLE FROM THE GROUPS
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleCode != null
                ? List.of(new SimpleGrantedAuthority(roleCode))
                : List.of();
    }

    //getter de grup
    public List<String> getGroups() {
       return groups;
    }

    // determinare role
    public String getRole() {
        return roleCode;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return uuid.toString();
    }

    public String getEmail() {
        return email;
    }

    public String getRoleId() {
        return roleId;
    }
}
