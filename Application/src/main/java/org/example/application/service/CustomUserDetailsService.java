package org.example.application.service;

import org.example.application.config.CustomUserDetails;
import org.example.application.repository.UserRepository;
import org.example.application.repository.entity.RoleEntity;
import org.example.application.repository.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    private static final Map<String, List<String>> HARDCODED_USER_GROUPS = Map.of(
            "a.a@a.a", List.of("grp-admins", "grp-users"),
            "b.b@b.b", List.of("grp-users"),
            "c.c@c.c", List.of("grp-users", "grp-users-2"),
            "d.d@d.d", List.of("grp-viewers")
    );

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    // After submit login, this function will be called, returning a new session
    // linked with de following CustomUserDetails
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Checks if email does not exist
        UserEntity userEntity = userRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found " + email));

        List<String> groups = HARDCODED_USER_GROUPS.getOrDefault(email, List.of());
        RoleEntity role = roleService.getStrongestRole(groups);
        String roleCode = role != null ? role.getCode() : null;
        String roleId = role != null ? role.getId().toString() : null;

        // Auto Checks for the password: if the form-entered one is the same as the one below
        return new CustomUserDetails(userEntity.getId(), userEntity.getEmail(), userEntity.getPasswordHash(), groups, roleCode, roleId);
    }
}
