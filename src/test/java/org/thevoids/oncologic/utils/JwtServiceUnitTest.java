package org.thevoids.oncologic.utils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

class JwtServiceUnitTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private String secretKey = "universidadicesiuniversidadicesiuniversidadicesi";
    private int expirationMinutes = 30;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
        jwtService.setSecret(secretKey);
        jwtService.setExpirationMinutes(expirationMinutes);
    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        // Arrange
        UserDetails userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateToken_InvalidUsername_ReturnsFalse() {
        // Arrange
        UserDetails userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        String token = jwtService.generateToken(userDetails);

        UserDetails differentUserDetails = new User("differentUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // Act
        boolean isValid = jwtService.validateToken(token, differentUserDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_ExpiredToken_ReturnsFalse() {
        // Arrange
        jwtService.setExpirationMinutes(-1); // Set expiration time in the past
        UserDetails userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        // String token = jwtService.generateToken(userDetails);

        String token = Jwts.builder()
            .setSubject("testuser") // Datos del usuario
            .claim("userId", 123)   // Datos personalizados
            .setIssuedAt(new Date()) // Fecha de emisión
            .setExpiration(new Date(System.currentTimeMillis() - (60 * 60 * 1000))) // Fecha de expiración (pasada)
            .signWith(new javax.crypto.spec.SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName()), SignatureAlgorithm.HS256) // Algoritmo de firma
            .compact();

        // Act
        boolean isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_InvalidToken_ReturnsFalse() {
        // Arrange
        String invalidToken = "invalid.token.value";

        UserDetails userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // Act
        boolean isValid = jwtService.validateToken(invalidToken, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_NullToken_ReturnsFalse() {
        // Arrange
        UserDetails userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // Act
        boolean isValid = jwtService.validateToken(null, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void generateToken_ReturnsValidToken() {
        // Arrange
        UserDetails userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_ReturnsCorrectUsername() {
        // Arrange
        UserDetails userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        String token = jwtService.generateToken(userDetails);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("testUser", username);
    }

    @Test
    void validateToken_UsernameDoesNotMatchAndTokenNotExpired_ReturnsFalse() {
        // Arrange
        UserDetails userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        String token = jwtService.generateToken(userDetails);

        UserDetails differentUserDetails = new User("differentUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // Act
        boolean isValid = jwtService.validateToken(token, differentUserDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void createClaims_ReturnsCorrectClaims() {
        // Arrange
        UserDetails userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // Act
        Map<String, Object> claims = jwtService.createClaims(userDetails);

        // Assert
        assertEquals("testUser", claims.get("identification"));
        assertEquals(List.of("ROLE_USER"), claims.get("roles"));
    }

    @Test
    void validateToken_UsernameMatchesAndTokenNotExpired_ReturnsTrue() {
        // Arrange
        UserDetails userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateToken_UsernameMatchesAndTokenExpired_ReturnsFalse() {
        // Arrange
        jwtService.setExpirationMinutes(-100); // Set expiration time in the past
        UserDetails userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertFalse(isValid); // Token should be expired
    }

    @Test
    void validateToken_UsernameDoesNotMatchAndTokenExpired_ReturnsFalse() {
        // Arrange
        jwtService.setExpirationMinutes(-100); // Set expiration time in the past
        UserDetails userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        String token = jwtService.generateToken(userDetails);

        UserDetails differentUserDetails = new User("differentUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // Act
        boolean isValid = jwtService.validateToken(token, differentUserDetails);

        // Assert
        assertFalse(isValid);
    }
}
