package org.thevoids.oncologic.security;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandlerUnitTest {

    private CustomAccessDeniedHandler accessDeniedHandler;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AccessDeniedException accessDeniedException;

    @BeforeEach
    void setUp() {
        accessDeniedHandler = new CustomAccessDeniedHandler();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        accessDeniedException = mock(AccessDeniedException.class);
    }

    @Test
    void testHandle_WithRefererWithoutError() throws IOException, ServletException {
        // Arrange
        when(request.getHeader("Referer")).thenReturn("http://localhost:8080/web/home");

        // Act
        accessDeniedHandler.handle(request, response, accessDeniedException);

        // Assert
        verify(response, times(1)).sendRedirect("http://localhost:8080/web/home?error=Access Denied");
    }

    @Test
    void testHandle_WithRefererWithError() throws IOException, ServletException {
        // Arrange
        when(request.getHeader("Referer")).thenReturn("http://localhost:8080/web/home?error=SomeError");

        // Act
        accessDeniedHandler.handle(request, response, accessDeniedException);

        // Assert
        verify(response, times(1)).sendRedirect("http://localhost:8080/web/home?error=SomeError");
    }

    @Test
    void testHandle_WithoutReferer() throws IOException, ServletException {
        // Arrange
        when(request.getHeader("Referer")).thenReturn(null);

        // Act
        accessDeniedHandler.handle(request, response, accessDeniedException);

        // Assert
        verify(response, times(1)).sendRedirect("/web/home?error=Access Denied");
    }
}
