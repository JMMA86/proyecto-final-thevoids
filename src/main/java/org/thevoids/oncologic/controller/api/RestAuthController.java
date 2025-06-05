package org.thevoids.oncologic.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thevoids.oncologic.dto.entity.AuthRequest;
import org.thevoids.oncologic.dto.custom.AuthResponseDTO;
import org.thevoids.oncologic.service.impl.CustomUserDetailsServiceImpl;
import org.thevoids.oncologic.utils.JwtService;
import org.thevoids.oncologic.dto.entity.RoleWithPermissionsDTO;
import org.thevoids.oncologic.dto.entity.PermissionDTO;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.service.UserService;
import org.thevoids.oncologic.service.AssignedRoles;
import org.thevoids.oncologic.service.RolePermissionService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "API para la gestión de autenticación de usuarios")
public class RestAuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private AssignedRoles assignedRolesService;

    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * Login method to authenticate user and generate JWT token.
     * 
     * @param request AuthRequest object containing username and password
     * @return JWT token if authentication is successful
     */
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y genera un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Parameter(description = "Datos de autenticación") @RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(request.getUsername());
            String token = jwtService.generateToken(userDetails);
            // Obtener el usuario por username
            User user = userService.getUserByIdentification(request.getUsername());
            // Obtener los roles del usuario
            List<Role> userRoles = assignedRolesService.getRolesFromUser(user.getUserId());
            // Convertir roles a DTO con permisos
            List<RoleWithPermissionsDTO> rolesWithPermissions = userRoles.stream()
                    .map(role -> {
                        List<Permission> rolePermissions = rolePermissionService
                                .getPermissionsFromRole(role.getRoleId());
                        List<PermissionDTO> permissionDTOs = rolePermissions.stream()
                                .map(permission -> new PermissionDTO(
                                        permission.getPermissionId(),
                                        permission.getPermissionName()))
                                .collect(Collectors.toList());
                        return new RoleWithPermissionsDTO(
                                role.getRoleId(),
                                role.getRoleName(),
                                permissionDTOs);
                    })
                    .collect(Collectors.toList());
            return ResponseEntity
                    .ok(new AuthResponseDTO(token, request.getUsername(), user.getUserId(), rolesWithPermissions));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
