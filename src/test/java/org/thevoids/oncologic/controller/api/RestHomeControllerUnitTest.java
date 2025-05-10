package org.thevoids.oncologic.controller.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.thevoids.oncologic.dto.custom.UserProfileDTO;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RestHomeControllerUnitTest {

    @InjectMocks
    private RestHomeController restHomeController;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserProfile_NoAuthentication_ReturnsError() {
        // Act
        ResponseEntity<UserProfileDTO> response = restHomeController.getUserProfile(null);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getUserProfile_NoRolesOrPermissions_ReturnsEmptyLists() {
        // Arrange
        String username = "testuser";
        when(authentication.getName()).thenReturn(username);
        when(authentication.getAuthorities()).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<UserProfileDTO> response = restHomeController.getUserProfile(authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserProfileDTO profile = response.getBody();
        assertNotNull(profile);
        assertEquals(username, profile.getUsername());
        assertTrue(profile.getRoles().isEmpty());
        assertTrue(profile.getPermissions().isEmpty());
    }
}
