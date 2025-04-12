package org.thevoids.oncologic.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.thevoids.oncologic.entity.User;

import java.util.Collection;
import java.util.List;

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
        return user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
