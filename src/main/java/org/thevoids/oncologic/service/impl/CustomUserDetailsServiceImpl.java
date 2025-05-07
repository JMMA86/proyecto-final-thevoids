package org.thevoids.oncologic.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.security.CustomUserDetail;
import org.thevoids.oncologic.service.UserService;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByIdentification(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetail(user);
    }
}