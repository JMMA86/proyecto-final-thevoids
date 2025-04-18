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

    @Test
    void validateToken_UsernameMatchesAndTokenNotExpired_ReturnsTrue() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(userDetails);
    
        // Act
        boolean isValid = jwtService.validateToken(token, userDetails);
    
        // Assert
        assertTrue(isValid);
    }
    
    @Test
    void validateToken_UsernameMatchesAndTokenExpired_ReturnsFalse() {
        // Arrange
        jwtService.setExpirationMinutes(-1);
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(userDetails);
    
        // Act
        boolean isValid = jwtService.validateToken(token, userDetails);
    
        // Assert
        assertFalse(isValid);
    }
    
    @Test
    void validateToken_UsernameDoesNotMatchAndTokenNotExpired_ReturnsFalse() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(userDetails);
    
        // Act
        boolean isValid = jwtService.validateToken(token, mock(CustomUserDetail.class)); // Different userDetails
    
        // Assert
        assertFalse(isValid);
    }
    
    @Test
    void validateToken_UsernameDoesNotMatchAndTokenExpired_ReturnsFalse() {
        // Arrange
        jwtService.setExpirationMinutes(-1);
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(userDetails);
    
        // Act
        boolean isValid = jwtService.validateToken(token, mock(CustomUserDetail.class)); // Different userDetails
    
        // Assert
        assertFalse(isValid);
    }

    @SuppressWarnings("unchecked")
    @Test
    void isTokenExpired_ExpiredToken_ReturnsTrue() {
        // Arrange
        jwtService.setExpirationMinutes(-1);
        when(userDetails.getUsername()).thenReturn("testuser");
        Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) List.of(new SimpleGrantedAuthority("ROLE_USER"));
        when(userDetails.getAuthorities()).thenReturn((Collection<GrantedAuthority>) authorities);

        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isExpired = jwtService.validateToken(token, userDetails);

        // Assert
        assertFalse(isExpired);
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