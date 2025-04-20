package org.thevoids.oncologic.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class AuthMessagesDTOUnitTest {

    @Test
    void testAuthRequestConstructorAndGetters() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";

        // Act
        AuthRequest authRequest = new AuthRequest(username, password);

        // Assert
        assertNotNull(authRequest);
        assertEquals(username, authRequest.getUsername());
        assertEquals(password, authRequest.getPassword());
    }

    @Test
    void testAuthRequestSetters() {
        // Arrange
        AuthRequest authRequest = new AuthRequest();

        // Act
        authRequest.setUsername("newUser");
        authRequest.setPassword("newPassword");

        // Assert
        assertEquals("newUser", authRequest.getUsername());
        assertEquals("newPassword", authRequest.getPassword());
    }

    @Test
    void testAuthResponseConstructorAndGetters() {
        // Arrange
        String accessToken = "sampleAccessToken";

        // Act
        AuthResponse authResponse = new AuthResponse(accessToken);

        // Assert
        assertNotNull(authResponse);
        assertEquals(accessToken, authResponse.getAccessToken());
    }

    @Test
    void testAuthResponseSetters() {
        // Arrange
        AuthResponse authResponse = new AuthResponse();

        // Act
        authResponse.setAccessToken("newAccessToken");

        // Assert
        assertEquals("newAccessToken", authResponse.getAccessToken());
    }
}
