package org.thevoids.oncologic.controller.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.thevoids.oncologic.dto.custom.ApiResponse;
import org.thevoids.oncologic.dto.custom.AuthResponseDTO;
import org.thevoids.oncologic.dto.entity.AuthRequest;
import org.thevoids.oncologic.service.impl.CustomUserDetailsServiceImpl;
import org.thevoids.oncologic.utils.JwtService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        ResponseEntity<ApiResponse<AuthResponseDTO>> response = restAuthController.login(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<AuthResponseDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isExito());
        assertEquals("Inicio de sesión exitoso", responseBody.getMensaje());
        
        AuthResponseDTO authResponse = responseBody.getDatos();
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

        doThrow(new RuntimeException("Invalid credentials")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act
        ResponseEntity<ApiResponse<AuthResponseDTO>> response = restAuthController.login(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<AuthResponseDTO> errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(false, errorResponse.isExito());
        assertEquals("Autenticación fallida: Invalid credentials", errorResponse.getMensaje());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(customUserDetailsServiceImpl, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).generateToken(any(UserDetails.class));
    }
}