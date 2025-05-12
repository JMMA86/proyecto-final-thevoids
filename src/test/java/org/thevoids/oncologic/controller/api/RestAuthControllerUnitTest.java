package org.thevoids.oncologic.controller.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.thevoids.oncologic.dto.custom.AuthResponseDTO;
import org.thevoids.oncologic.dto.entity.AuthRequest;
import org.thevoids.oncologic.service.impl.CustomUserDetailsServiceImpl;
import org.thevoids.oncologic.utils.JwtService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RestAuthControllerUnitTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private RestAuthController restAuthController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_SuccessfulAuthentication_ReturnsToken() {
        // Arrange
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        when(customUserDetailsServiceImpl.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("mocked-jwt-token");

        // Act
        ResponseEntity<AuthResponseDTO> response = restAuthController.login(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponseDTO authResponse = response.getBody();
        assertNotNull(authResponse);
        assertEquals("mocked-jwt-token", authResponse.getToken());
        assertEquals("testuser", authResponse.getUsername());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(customUserDetailsServiceImpl, times(1)).loadUserByUsername("testuser");
        verify(jwtService, times(1)).generateToken(userDetails);
    }

    @Test
    void login_FailedAuthentication_ReturnsError() {
        // Arrange
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        doThrow(new BadCredentialsException("Invalid credentials")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act
        ResponseEntity<AuthResponseDTO> response = restAuthController.login(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(customUserDetailsServiceImpl, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).generateToken(any(UserDetails.class));
    }

    @Test
    void login_ExceptionThrown_ReturnsInternalServerError() {
        // Arrange
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("password");
    
        doThrow(new RuntimeException("Test exception")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act
        ResponseEntity<AuthResponseDTO> response = restAuthController.login(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(customUserDetailsServiceImpl, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).generateToken(any(UserDetails.class));
    }
    
}