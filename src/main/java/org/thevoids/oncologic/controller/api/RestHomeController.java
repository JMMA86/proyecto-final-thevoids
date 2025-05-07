package org.thevoids.oncologic.controller.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
public class RestHomeController {

    /**
     * Gets the current user's profile information including username, roles, and permissions.
     *
     * @param authentication the authenticated user.
     * @return a response with the user's profile information.
     */
    @GetMapping
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
        }

        // Extract roles (prefixed with ROLE_)
        List<String> roles = authentication.getAuthorities().stream()
                .filter(auth -> auth.getAuthority().startsWith("ROLE_"))
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Extract permissions (not prefixed with ROLE_)
        List<String> permissions = authentication.getAuthorities().stream()
                .filter(auth -> !auth.getAuthority().startsWith("ROLE_"))
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> profile = Map.of(
                "username", authentication.getName(),
                "roles", roles,
                "permissions", permissions
        );

        return ResponseEntity.ok(profile);
    }
}