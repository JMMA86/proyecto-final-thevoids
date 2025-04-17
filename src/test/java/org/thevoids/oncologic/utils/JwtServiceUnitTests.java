package org.thevoids.oncologic.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.thevoids.oncologic.security.CustomUserDetail;

import java.util.List;
import java.util.Map;

import java.util.Collections;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceUnitTests {

    private JwtService jwtService;

    @Mock
    private CustomUserDetail userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
        jwtService.setSecret("mysecretkeymysecretkeymysecretkeymysecretkey");
        jwtService.setExpirationMinutes(60);
    }

    @Test
    void generateToken_ReturnsValidToken() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    void extractUsername_ReturnsCorrectUsername() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("testuser");
        Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) List.of(new SimpleGrantedAuthority("ROLE_USER"));
        when(userDetails.getAuthorities()).thenReturn((Collection<GrantedAuthority>) authorities);

        String token = jwtService.generateToken(userDetails);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("testuser", username);
    }

    @SuppressWarnings("unchecked")
    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("testuser");
        Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) List.of(new SimpleGrantedAuthority("ROLE_USER"));
        when(userDetails.getAuthorities()).thenReturn((Collection<GrantedAuthority>) authorities);

        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @SuppressWarnings("unchecked")
    @Test
    void validateToken_InvalidToken_ReturnsFalse() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("testuser");
        Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) List.of(new SimpleGrantedAuthority("ROLE_USER"));
        when(userDetails.getAuthorities()).thenReturn((Collection<GrantedAuthority>) authorities);

        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.validateToken(token + "tampered", userDetails);

        // Assert
        assertFalse(isValid);
    }

    @SuppressWarnings("unchecked")
    @Test
    void isTokenExpired_ExpiredToken_ReturnsTrue() {
        // Arrange
        jwtService.setExpirationMinutes(-1); // Set expiration in the past
        when(userDetails.getUsername()).thenReturn("testuser");
        Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) List.of(new SimpleGrantedAuthority("ROLE_USER"));
        when(userDetails.getAuthorities()).thenReturn((Collection<GrantedAuthority>) authorities);

        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isExpired = jwtService.validateToken(token, userDetails);

        // Assert
        assertFalse(isExpired); // Token should be invalid due to expiration
    }

    @SuppressWarnings("unchecked")
    @Test
    void createClaims_ReturnsCorrectClaims() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("testuser");
        Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) List.of(new SimpleGrantedAuthority("ROLE_USER"));
        when(userDetails.getAuthorities()).thenReturn((Collection<GrantedAuthority>) authorities);


        // Act
        Map<String, Object> claims = jwtService.createClaims(userDetails);

        // Assert
        assertEquals("testuser", claims.get("identification"));
        assertEquals(List.of("ROLE_USER"), claims.get("roles"));
    }
}