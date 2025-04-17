package org.thevoids.oncologic.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.thevoids.oncologic.entity.User;

public class CustomUserDetail implements UserDetails {
    
    private User user;

    public CustomUserDetail(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getIdentification();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return user.getAssignedRoles().stream()
                .map(assignedRole -> new SimpleGrantedAuthority(assignedRole.getRole().getRoleName()))
                .collect(Collectors.toList());
    }
}
