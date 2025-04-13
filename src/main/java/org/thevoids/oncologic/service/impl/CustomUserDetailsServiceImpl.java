package org.thevoids.oncologic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByIdentification(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    
        // Obtener los roles asignados al usuario y mapearlos a GrantedAuthority
        List<GrantedAuthority> authorities = user.getAssignedRoles().stream()
                .map(assignedRole -> new SimpleGrantedAuthority(assignedRole.getRole().getRoleName()))
                .collect(Collectors.toList());
    
        return new org.springframework.security.core.userdetails.User(
                user.getIdentification(),
                user.getPassword(),
                authorities
        );
    }
}
