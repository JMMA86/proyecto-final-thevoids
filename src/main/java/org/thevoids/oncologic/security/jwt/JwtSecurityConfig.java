package org.thevoids.oncologic.security.jwt;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.thevoids.oncologic.security.CustomAccessDeniedHandler;
import org.thevoids.oncologic.service.impl.CustomUserDetailsServiceImpl;
import org.thevoids.oncologic.utils.JwtService;

@Configuration
public class JwtSecurityConfig {

    private final CustomUserDetailsServiceImpl userDetailsService;

    public JwtSecurityConfig(CustomUserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("http://localhost:5173"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                configuration.setAllowedHeaders(List.of("*"));
                return configuration;
            }))
            .securityMatcher("/api/v1/**")
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests(
                auth -> auth
                    .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .requestMatchers("/api/v1/users/**").hasAuthority("VIEW_USERS")
                    .requestMatchers("/api/v1/roles/**").hasAuthority("VIEW_ROLES")
                    .requestMatchers("/api/v1/permissions/**").hasAuthority("VIEW_PERMISSIONS")
                    .requestMatchers("/api/v1/schedule/**").hasAuthority("VIEW_SCHEDULES")
                    .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .exceptionHandling(handling -> handling
                .accessDeniedHandler(new CustomAccessDeniedHandler()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsServiceImpl customUserDetailsServiceImpl) {
        return new JwtAuthenticationFilter(jwtService, customUserDetailsServiceImpl);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
