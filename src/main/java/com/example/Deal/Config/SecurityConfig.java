package com.example.Deal.Config;

import com.example.Deal.Filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Contains elements describing main security configurations.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http.csrf(csrf -> csrf.disable())
                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authorizeHttpRequests(authorize -> authorize
                         .requestMatchers("/swagger-ui/**").permitAll()
                         .requestMatchers("/v3/api-docs").permitAll())
                 .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withRolePrefix("")
                .role("CREDIT_USER").implies("USER")
                .role("OVERDRAFT_USER").implies("USER")
                .role("DEAL_SUPERUSER").implies("CREDIT_USER")
                .role("DEAL_SUPERUSER").implies("OVERDRAFT_USER")
                .role("CONTRACTOR_RUS").implies("USER")
                .role("CONTRACTOR_SUPERUSER").implies("USER")
                .role("SUPERUSER").implies("DEAL_SUPERUSER")
                .build();
    }

    /**
     * Sets up GrantedAuthorityDefaults
     * <p>
     * Removes "ROLE_" prefix for role checking (while authorization).
     *
     * @return GrantedAuthorityDefault instance
     */
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

}
