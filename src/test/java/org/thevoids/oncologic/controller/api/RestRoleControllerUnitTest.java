package org.thevoids.oncologic.controller.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.RoleDTO;
import org.thevoids.oncologic.dto.UserResponseDTO;
import org.thevoids.oncologic.dto.ErrorResponse;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.repository.AssignedRoleRepository;
import org.thevoids.oncologic.repository.RoleRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.mapper.UserMapper;
import org.thevoids.oncologic.service.AssignedRoles;
import org.thevoids.oncologic.service.RoleService;
import org.thevoids.oncologic.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestRoleControllerUnitTest {

    @InjectMocks
    private RestRoleController restRoleController;

    @Mock
    private RoleService roleService;

    @Mock
    private UserService userService;

    @Mock
    private AssignedRoles assignedRolesService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AssignedRoleRepository assignedRoleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserResponseDTO userResponseDTO;

    private Role adminRole;
    private Role userRole;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize shared objects
        adminRole = new Role();
        adminRole.setRoleId(1L);
        adminRole.setRoleName("Admin");

        userRole = new Role();
        userRole.setRoleId(2L);
        userRole.setRoleName("User");

        testUser = new User();
        testUser.setUserId(1L);
        testUser.setFullName("John Doe");
    }

    @Test
    void testGetAllRoles_Success() {
        // Arrange
        when(roleService.getAllRoles()).thenReturn(Arrays.asList(adminRole, userRole));

        // Act
        ResponseEntity<?> response = restRoleController.getAllRoles();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        List<RoleDTO> roleDTOs = (List<RoleDTO>) response.getBody();
        assertNotNull(roleDTOs);
        assertEquals(2, roleDTOs.size());
        assertEquals("Admin", roleDTOs.get(0).getRoleName());
        assertEquals("User", roleDTOs.get(1).getRoleName());
    }

    @Test
    void testGetAllRoles_Failure() {
        // Arrange
        when(roleService.getAllRoles()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = restRoleController.getAllRoles();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Failed to retrieve roles", errorResponse.getError());
    }

    @Test
    void testGetRoleById_Success() {
        // Arrange
        when(roleService.getRole(1L)).thenReturn(adminRole);

        // Act
        ResponseEntity<?> response = restRoleController.getRoleById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RoleDTO roleDTO = (RoleDTO) response.getBody();
        assertNotNull(roleDTO);
        assertEquals(1L, roleDTO.getRoleId());
        assertEquals("Admin", roleDTO.getRoleName());
    }

    @Test
    void testGetRoleById_NotFound() {
        // Arrange
        when(roleService.getRole(1L)).thenThrow(new RuntimeException("Role not found"));

        // Act
        ResponseEntity<?> response = restRoleController.getRoleById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Role not found", errorResponse.getError());
    }

    @Test
    void testCreateRole_Success() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(null, "Admin");
        when(roleService.createRole(any(Role.class))).thenReturn(adminRole);

        // Act
        ResponseEntity<?> response = restRoleController.createRole(roleDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RoleDTO createdRoleDTO = (RoleDTO) response.getBody();
        assertNotNull(createdRoleDTO);
        assertEquals(1L, createdRoleDTO.getRoleId());
        assertEquals("Admin", createdRoleDTO.getRoleName());
    }

    @Test
    void testCreateRole_Failure() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(null, "Admin");
        when(roleService.createRole(any(Role.class))).thenThrow(new RuntimeException("Failed to create role"));

        // Act
        ResponseEntity<?> response = restRoleController.createRole(roleDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Failed to create role", errorResponse.getError());
    }

    @Test
    void testAssignRoleToUser_Success() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(roleService.getRole(1L)).thenReturn(adminRole);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(roleRepository.existsById(1L)).thenReturn(true);
        when(assignedRoleRepository.existsByRoleIdAndUserId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(testUser));
        when(roleRepository.findById(1L)).thenReturn(java.util.Optional.of(adminRole));
        when(assignedRoleRepository.save(any())).thenReturn(null);
        when(userMapper.toUserResponseDTO(any())).thenReturn(new UserResponseDTO(testUser.getUserId(), testUser.getFullName(), null, null, null));

        // Act
        ResponseEntity<?> response = restRoleController.assignRoleToUser(1L, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testAssignRoleToUser_RoleNotFound() {
        // Arrange
        when(roleService.getRole(1L)).thenReturn(null);
    
        // Act
        ResponseEntity<?> response = restRoleController.assignRoleToUser(1L, 1L);
    
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Role not found", errorResponse.getError());
    }
    
    @Test
    void testAssignRoleToUser_UserNotFound() {
        // Arrange
        when(roleService.getRole(1L)).thenReturn(adminRole);
        when(userService.getUserById(1L)).thenReturn(null);
    
        // Act
        ResponseEntity<?> response = restRoleController.assignRoleToUser(1L, 1L);
    
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("User not found", errorResponse.getError());
    }
    
    @Test
    void testAssignRoleToUser_AssignmentFailure() {
        // Arrange
        when(roleService.getRole(1L)).thenReturn(adminRole);
        when(userService.getUserById(1L)).thenReturn(testUser);
        doThrow(new RuntimeException("Failed to assign role")).when(assignedRolesService).assignRoleToUser(1L, 1L);
    
        // Act
        ResponseEntity<?> response = restRoleController.assignRoleToUser(1L, 1L);
    
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Failed to assign role", errorResponse.getError());
    }
}