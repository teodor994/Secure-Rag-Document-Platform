package org.example.application.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.Session;
import org.example.application.config.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class SecurityController {
    private final JdbcIndexedSessionRepository sessionRepository;

    public SecurityController(JdbcIndexedSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    // Invalidates the stored session ID by removing session from DB
    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        if (request.getSession(false) == null) {
            response.put("authenticated", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        response.put("authenticated", true);
        response.put("logout", "success");

        sessionRepository.deleteById(request.getSession().getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Opens the login form if no session cookie present, otherwise return session details
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUserAuthentication(@AuthenticationPrincipal CustomUserDetails authentication) {
        Map<String, Object> response = new HashMap<>();
        // No cookie found -> auth required
        if (authentication == null) {
            response.put("authenticated", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        // cookie found, we return all the details for the front-end
        response.put("authenticated", true);
        response.put("email", authentication.getEmail());
        response.put("userId", authentication.getUsername());
        // sa se afiseze si grupul
        response.put("groups", authentication.getGroups());

        // afisare rol
        response.put("role", authentication.getRole());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
