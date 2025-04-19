package org.thevoids.oncologic.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        // Get the referer URL
        String referer = request.getHeader("Referer");

        // Avoid adding duplicate error messages
        if (referer != null && !referer.contains("error=")) {
            String separator = referer.contains("?") ? "&" : "?";
            response.sendRedirect(referer + separator + "error=Access Denied");
        } else if (referer != null) {
            response.sendRedirect(referer); // Redirect without adding duplicate error
        } else {
            response.sendRedirect("/web/home?error=Access Denied"); // Redirect to a safe default page
        }
    }
}
