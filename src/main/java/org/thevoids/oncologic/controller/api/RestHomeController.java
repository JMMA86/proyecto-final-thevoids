package org.thevoids.oncologic.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thevoids.oncologic.dto.custom.UserProfileDTO;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "Perfil", description = "API para la gestión del perfil de usuario")
public class RestHomeController {

    /**
     * Gets the current user's profile information including username, roles, and permissions.
     *
     * @param authentication the authenticated user.
     * @return a response with the user's profile information.
     */
    @Operation(summary = "Obtener perfil de usuario", description = "Recupera la información del perfil del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil recuperado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDTO.class))),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @GetMapping
    public ResponseEntity<UserProfileDTO> getUserProfile(
        @Parameter(description = "Datos de autenticación")
        Authentication authentication
    ) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extract roles (prefixed with ROLE_)
        List<String> roles = authentication.getAuthorities().stream()
                .filter(auth -> auth.getAuthority().startsWith("ROLE_"))
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Extract permissions (not prefixed with ROLE_)
        List<String> permissions = authentication.getAuthorities().stream()
                .filter(auth -> !auth.getAuthority().startsWith("ROLE_"))
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        UserProfileDTO profile = new UserProfileDTO(
                authentication.getName(),
                roles,
                permissions
        );

        return ResponseEntity.ok(profile);
    }
}