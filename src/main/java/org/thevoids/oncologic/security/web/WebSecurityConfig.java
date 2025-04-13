package org.thevoids.oncologic.security.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain sessionSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/web/**") 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/web/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/web/auth/login") 
                .defaultSuccessUrl("/web/home")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/web/auth/logout")
                .logoutSuccessUrl("/web/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}