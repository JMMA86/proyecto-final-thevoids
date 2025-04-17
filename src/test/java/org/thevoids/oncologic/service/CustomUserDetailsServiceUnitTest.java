package org.thevoids.oncologic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.security.CustomUserDetail;
import org.thevoids.oncologic.service.impl.CustomUserDetailsServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceUnitTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_WhenUserExists_ReturnsUserDetails() {
        // Arrange
        User user = new User();
        user.setIdentification("123456");
        user.setPassword("password");
        when(userService.getUserByIdentification("123456")).thenReturn(user);

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("123456");

        // Assert
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof CustomUserDetail);
        assertEquals("123456", userDetails.getUsername());
        verify(userService, times(1)).getUserByIdentification("123456");
    }

    @Test
    void loadUserByUsername_WhenUserDoesNotExist_ThrowsException() {
        // Arrange
        when(userService.getUserByIdentification("123456")).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("123456");
        });

        assertEquals("User not found", exception.getMessage());
        verify(userService, times(1)).getUserByIdentification("123456");
    }
}