package org.thevoids.oncologic.controller.api;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.custom.ApiResponse;
import org.thevoids.oncologic.dto.entity.RoleDTO;
import org.thevoids.oncologic.dto.entity.RoleWithPermissionsDTO;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.mapper.RoleMapper;
import org.thevoids.oncologic.mapper.PermissionMapper;
import org.thevoids.oncologic.service.RoleService;

class RestRoleControllerUnitTest {

    @InjectMocks
    private RestRoleController restRoleController;

    @Mock
    private RoleService roleService;
    
    @Mock
    private RoleMapper roleMapper;
    
    @Mock
    private PermissionMapper permissionMapper;

    private Role adminRole;
    private Role userRole;
    private Permission viewPermission;

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
        
        viewPermission = new Permission();
        viewPermission.setPermissionId(1L);
        viewPermission.setPermissionName("VIEW_PERMISSIONS");
    }
    
    @Test
    void testGetAllRoles_Success() {
        // Arrange
        List<RoleDTO> expectedRoles = Arrays.asList(
            new RoleDTO(1L, "Admin"),
            new RoleDTO(2L, "User")
        );
        when(roleService.getAllRoles()).thenReturn(Arrays.asList(adminRole, userRole));
        when(roleMapper.toRoleDTO(adminRole)).thenReturn(expectedRoles.get(0));
        when(roleMapper.toRoleDTO(userRole)).thenReturn(expectedRoles.get(1));

        // Act
        ResponseEntity<ApiResponse<List<RoleDTO>>> response = restRoleController.getAllRoles();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<List<RoleDTO>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Roles recuperados con éxito", responseBody.getMensaje());
        assertTrue(responseBody.isExito());
        
        List<RoleDTO> roles = responseBody.getDatos();
        assertEquals(2, roles.size());
        assertEquals("Admin", roles.get(0).getRoleName());
        assertEquals("User", roles.get(1).getRoleName());
    }

    @Test
    void testGetAllRoles_Failure() {
        // Arrange
        when(roleService.getAllRoles()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<ApiResponse<List<RoleDTO>>> response = restRoleController.getAllRoles();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ApiResponse<List<RoleDTO>> errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Error al recuperar los roles: Database error", errorResponse.getMensaje());
        assertEquals(false, errorResponse.isExito());
    }
    
    @Test
    void testGetRoleById_Success() {
        // Arrange
        RoleWithPermissionsDTO expectedRole = new RoleWithPermissionsDTO(1L, "Admin", new ArrayList<>());
        when(roleService.getRole(1L)).thenReturn(adminRole);
        when(roleMapper.toRoleWithPermissionsDTO(adminRole)).thenReturn(expectedRole);

        // Act
        ResponseEntity<ApiResponse<RoleWithPermissionsDTO>> response = restRoleController.getRoleById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<RoleWithPermissionsDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Rol recuperado con éxito", responseBody.getMensaje());
        assertTrue(responseBody.isExito());
        
        RoleWithPermissionsDTO role = responseBody.getDatos();
        assertEquals(1L, role.getRoleId());
        assertEquals("Admin", role.getRoleName());
    }

    @Test
    void testGetRoleById_NotFound() {
        // Arrange
        when(roleService.getRole(1L)).thenThrow(new RuntimeException("Role not found"));

        // Act
        ResponseEntity<ApiResponse<RoleWithPermissionsDTO>> response = restRoleController.getRoleById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ApiResponse<RoleWithPermissionsDTO> errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Error al recuperar el rol: Role not found", errorResponse.getMensaje());
        assertEquals(false, errorResponse.isExito());
    }
    
    @Test
    void testCreateRole_Success() {
        // Arrange
        RoleDTO inputRoleDTO = new RoleDTO(null, "Admin");
        RoleDTO expectedRoleDTO = new RoleDTO(1L, "Admin");
        
        when(roleService.createRole(any(Role.class))).thenReturn(adminRole);
        when(roleMapper.toRoleDTO(adminRole)).thenReturn(expectedRoleDTO);

        // Act
        ResponseEntity<ApiResponse<RoleDTO>> response = restRoleController.createRole(inputRoleDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ApiResponse<RoleDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Rol creado con éxito", responseBody.getMensaje());
        assertTrue(responseBody.isExito());
        
        RoleDTO createdRole = responseBody.getDatos();
        assertEquals(1L, createdRole.getRoleId());
        assertEquals("Admin", createdRole.getRoleName());
    }

    @Test
    void testCreateRole_Failure() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(null, "Admin");
        when(roleService.createRole(any(Role.class))).thenThrow(new RuntimeException("Failed to create role"));

        // Act
        ResponseEntity<ApiResponse<RoleDTO>> response = restRoleController.createRole(roleDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<RoleDTO> errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Error al crear el rol: Failed to create role", errorResponse.getMensaje());
        assertEquals(false, errorResponse.isExito());
    }

    @Test
    void testUpdateRole_Success() {
        // Arrange
        RoleDTO inputRoleDTO = new RoleDTO(1L, "Updated Admin");
        RoleDTO expectedRoleDTO = new RoleDTO(1L, "Updated Admin");
        
        when(roleService.getRole(1L)).thenReturn(adminRole);
        when(roleService.updateRole(any(Role.class))).thenReturn(adminRole);
        when(roleMapper.toRoleDTO(adminRole)).thenReturn(expectedRoleDTO);

        // Act
        ResponseEntity<ApiResponse<RoleDTO>> response = restRoleController.updateRole(1L, inputRoleDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<RoleDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Rol actualizado con éxito", responseBody.getMensaje());
        assertTrue(responseBody.isExito());
        
        RoleDTO updatedRole = responseBody.getDatos();
        assertEquals(1L, updatedRole.getRoleId());
        assertEquals("Updated Admin", updatedRole.getRoleName());
    }

    @Test
    void testUpdateRole_Failure() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(1L, "Updated Admin");
        when(roleService.getRole(1L)).thenThrow(new RuntimeException("Failed to update role"));

        // Act
        ResponseEntity<ApiResponse<RoleDTO>> response = restRoleController.updateRole(1L, roleDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<RoleDTO> errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Error al actualizar el rol: Failed to update role", errorResponse.getMensaje());
        assertEquals(false, errorResponse.isExito());
    }

    @Test
    void testDeleteRole_Success() {
        // Arrange
        when(roleService.getRole(1L)).thenReturn(adminRole);

        // Act
        ResponseEntity<ApiResponse<Void>> response = restRoleController.deleteRole(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<Void> successResponse = response.getBody();
        assertNotNull(successResponse);
        assertEquals("Rol eliminado con éxito", successResponse.getMensaje());
        assertTrue(successResponse.isExito());
    }

    @Test
    void testDeleteRole_Failure() {
        // Arrange
        when(roleService.getRole(1L)).thenThrow(new RuntimeException("Failed to delete role"));

        // Act
        ResponseEntity<ApiResponse<Void>> response = restRoleController.deleteRole(1L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<Void> errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Error al eliminar el rol: Failed to delete role", errorResponse.getMensaje());
        assertEquals(false, errorResponse.isExito());
    }
}