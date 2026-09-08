package org.example.application.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

/* Any request will be going through this filter here */
@Configuration
@EnableWebSecurity
// pentru @PreAuthorize("hasAuthority('ADMIN')")
@EnableMethodSecurity
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 3000)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        return http
                // REMEMBER TO ENABLE CSRF
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(authorizeReq ->authorizeReq
                                .requestMatchers("/ui", "/ui/**").permitAll()
                                .requestMatchers("/auth/me").permitAll()
                                .requestMatchers("/login", "/login?error", "/error").permitAll()
                                .requestMatchers("/sse", "/sse/**", "/mcp/**").permitAll()
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().authenticated()
                        )
                .formLogin(form -> form
                        .defaultSuccessUrl("/ui", true)
                        .permitAll())
                // When unauthorized users (no cookie) enters on a secured url
                .exceptionHandling(e -> e
                        .defaultAuthenticationEntryPointFor(
                                (request, response, authException) -> {
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"authenticated\": false}");
                                },
                                request -> request.getRequestURI().startsWith(request.getContextPath() + "/api/")
                        ))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
