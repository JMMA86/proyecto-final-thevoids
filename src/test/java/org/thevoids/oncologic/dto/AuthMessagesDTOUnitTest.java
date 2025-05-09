package org.thevoids.oncologic.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.thevoids.oncologic.dto.entity.AuthRequest;
import org.thevoids.oncologic.dto.custom.AuthResponseDTO;

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
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(accessToken, "testUser");

        // Assert
        assertNotNull(authResponseDTO);
        assertEquals(accessToken, authResponseDTO.getToken());
    }

    @Test
    void testAuthResponseSetters() {
        // Arrange
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();

        // Act
        authResponseDTO.setToken("newAccessToken");
        // Assert
        assertEquals("newAccessToken", authResponseDTO.getToken());
    }
}
