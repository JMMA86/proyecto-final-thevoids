package org.thevoids.oncologic.security.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.thevoids.oncologic.security.CustomAccessDeniedHandler;
import org.thevoids.oncologic.service.impl.CustomUserDetailsServiceImpl;

@Configuration
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain sessionSecurityFilterChain(HttpSecurity http, AccessDeniedHandler accessDeniedHandler) throws Exception {
        http
            .securityMatcher("/web/**")
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                    .requestMatchers("/web/users/**").hasAuthority("VIEW_USERS")
                    .requestMatchers("/web/roles/**").hasAuthority("VIEW_ROLES")
                    .requestMatchers("/web/permissions/**").hasAuthority("VIEW_PERMISSIONS")
                    .requestMatchers("/web/admin/**").hasRole("ADMIN")
                    .requestMatchers("/web/auth/**").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/web/auth/login")
                    .defaultSuccessUrl("/web/home", true) // Redirect to a page the user has access to
                    .failureUrl("/web/auth/login?error=Bad%20Credentials") // Add error parameter on login failure
                    .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/web/auth/logout")
                    .logoutSuccessUrl("/web/auth/login?logout")
                    .permitAll()
            )
            .exceptionHandling(handling -> handling
                .accessDeniedHandler(new CustomAccessDeniedHandler())) // Ensure the handler is correctly configured
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}