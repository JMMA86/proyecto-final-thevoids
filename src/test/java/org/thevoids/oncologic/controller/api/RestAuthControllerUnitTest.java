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
import org.thevoids.oncologic.dto.entity.RoleWithPermissionsDTO;
import org.thevoids.oncologic.service.impl.CustomUserDetailsServiceImpl;
import org.thevoids.oncologic.utils.JwtService;
import org.thevoids.oncologic.service.UserService;
import org.thevoids.oncologic.service.AssignedRoles;
import org.thevoids.oncologic.service.RolePermissionService;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.Permission;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class RestAuthControllerUnitTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private AssignedRoles assignedRolesService;

    @Mock
    private RolePermissionService rolePermissionService;

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
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setEmail("testuser");

        Role mockRole = new Role();
        mockRole.setRoleId(1L);
        mockRole.setRoleName("ADMIN");

        Permission mockPermission = new Permission();
        mockPermission.setPermissionId(1L);
        mockPermission.setPermissionName("READ_USERS");

        when(customUserDetailsServiceImpl.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("mocked-jwt-token");
        when(userService.getUserByIdentification("testuser")).thenReturn(mockUser);
        when(assignedRolesService.getRolesFromUser(1L)).thenReturn(Arrays.asList(mockRole));
        when(rolePermissionService.getPermissionsFromRole(1L)).thenReturn(Arrays.asList(mockPermission));

        // Act
        ResponseEntity<AuthResponseDTO> response = restAuthController.login(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponseDTO authResponse = response.getBody();
        assertNotNull(authResponse);
        assertEquals("mocked-jwt-token", authResponse.getToken());
        assertEquals("testuser", authResponse.getUsername());
        
        // Verificar roles y permisos
        assertNotNull(authResponse.getRoles());
        assertEquals(1, authResponse.getRoles().size());
        RoleWithPermissionsDTO role = authResponse.getRoles().get(0);
        assertEquals(1L, role.getRoleId());
        assertEquals("ADMIN", role.getRoleName());
        assertEquals(1, role.getPermissions().size());
        assertEquals("READ_USERS", role.getPermissions().get(0).getPermissionName());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(customUserDetailsServiceImpl, times(1)).loadUserByUsername("testuser");
        verify(jwtService, times(1)).generateToken(userDetails);
        verify(userService, times(1)).getUserByIdentification("testuser");
        verify(assignedRolesService, times(1)).getRolesFromUser(1L);
        verify(rolePermissionService, times(1)).getPermissionsFromRole(1L);
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
        verify(userService, never()).getUserByIdentification(anyString());
        verify(assignedRolesService, never()).getRolesFromUser(anyLong());
        verify(rolePermissionService, never()).getPermissionsFromRole(anyLong());
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
        verify(userService, never()).getUserByIdentification(anyString());
        verify(assignedRolesService, never()).getRolesFromUser(anyLong());
        verify(rolePermissionService, never()).getPermissionsFromRole(anyLong());
    }
}