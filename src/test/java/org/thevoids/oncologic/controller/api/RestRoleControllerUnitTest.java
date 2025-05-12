package org.thevoids.oncologic.controller.api;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.entity.RoleDTO;
import org.thevoids.oncologic.dto.entity.RoleWithPermissionsDTO;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.ResourceAlreadyExistsException;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.mapper.RoleMapper;
import org.thevoids.oncologic.repository.PermissionRepository;
import org.thevoids.oncologic.repository.RolePermissionRepository;
import org.thevoids.oncologic.repository.RoleRepository;
import org.thevoids.oncologic.mapper.PermissionMapper;
import org.thevoids.oncologic.service.RoleService;
import org.thevoids.oncologic.service.RolePermissionService;

class RestRoleControllerUnitTest {

    @InjectMocks
    private RestRoleController restRoleController;

    @Mock
    private RoleService roleService;
    
    @Mock
    private RoleMapper roleMapper;
    
    @Mock
    private PermissionMapper permissionMapper;

    @Mock
    private RolePermissionService rolePermissionService;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @Mock
    private RoleRepository roleRepository;

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
        ResponseEntity<List<RoleDTO>> response = restRoleController.getAllRoles();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<RoleDTO> roles = response.getBody();
        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertEquals("Admin", roles.get(0).getRoleName());
        assertEquals("User", roles.get(1).getRoleName());
    }

    @Test
    void testGetAllRoles_Failure() {
        // Arrange
        when(roleService.getAllRoles()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<List<RoleDTO>> response = restRoleController.getAllRoles();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    
    @Test
    void testGetRoleById_Success() {
        // Arrange
        RoleWithPermissionsDTO expectedRole = new RoleWithPermissionsDTO(1L, "Admin", new ArrayList<>());
        when(roleService.getRole(1L)).thenReturn(adminRole);
        when(roleMapper.toRoleWithPermissionsDTO(adminRole)).thenReturn(expectedRole);

        // Act
        ResponseEntity<RoleWithPermissionsDTO> response = restRoleController.getRoleById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RoleWithPermissionsDTO role = response.getBody();
        assertNotNull(role);
        assertEquals(1L, role.getRoleId());
        assertEquals("Admin", role.getRoleName());
    }

    @Test
    void testGetRoleById_NotFound() {
        // Arrange
        when(roleService.getRole(1L)).thenThrow(new ResourceNotFoundException("Rol", "id", 1L));

        // Act
        ResponseEntity<RoleWithPermissionsDTO> response = restRoleController.getRoleById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test   
    void testGetRoleById_InternalServerError_ReturnsError() {
        // Arrange
        when(roleService.getRole(1L))
            .thenThrow(new RuntimeException("Error interno del servidor"));

        // Act
        ResponseEntity<RoleWithPermissionsDTO> response = restRoleController.getRoleById(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    
    @Test
    void testCreateRole_Success() {
        // Arrange
        RoleDTO inputRoleDTO = new RoleDTO(null, "Admin");
        RoleDTO expectedRoleDTO = new RoleDTO(1L, "Admin");
        
        when(roleService.createRole(any(Role.class))).thenReturn(adminRole);
        when(roleMapper.toRoleDTO(adminRole)).thenReturn(expectedRoleDTO);

        // Act
        ResponseEntity<RoleDTO> response = restRoleController.createRole(inputRoleDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        RoleDTO createdRole = response.getBody();
        assertNotNull(createdRole);
        assertEquals(1L, createdRole.getRoleId());
        assertEquals("Admin", createdRole.getRoleName());
    }

    @Test
    void testCreateRole_Failure() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(null, "Admin");
        when(roleService.createRole(any(Role.class))).thenThrow(new ResourceAlreadyExistsException("Rol", "nombre", "Admin"));

        // Act
        ResponseEntity<RoleDTO> response = restRoleController.createRole(roleDTO);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test   
    void testCreateRole_InternalServerError_ReturnsError() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(null, "Admin");
        when(roleService.createRole(any(Role.class)))
            .thenThrow(new RuntimeException("Error interno del servidor"));

        // Act
        ResponseEntity<RoleDTO> response = restRoleController.createRole(roleDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
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
        ResponseEntity<RoleDTO> response = restRoleController.updateRole(1L, inputRoleDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RoleDTO updatedRole = response.getBody();
        assertNotNull(updatedRole);
        assertEquals(1L, updatedRole.getRoleId());
        assertEquals("Updated Admin", updatedRole.getRoleName());
    }

    @Test
    void testUpdateRole_Failure() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(1L, "Updated Admin");
        when(roleService.getRole(1L)).thenThrow(new ResourceNotFoundException("Rol", "id", 1L));

        // Act
        ResponseEntity<RoleDTO> response = restRoleController.updateRole(1L, roleDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test   
    void testUpdateRole_InternalServerError_ReturnsError() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(1L, "Updated Admin");
        when(roleService.getRole(1L)).thenReturn(adminRole);
        doThrow(new RuntimeException("Error interno del servidor"))
            .when(roleService).updateRole(any(Role.class));

        // Act
        ResponseEntity<RoleDTO> response = restRoleController.updateRole(1L, roleDTO);

        // Assert   
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testDeleteRole_Success() {
        // Arrange
        when(roleService.getRole(1L)).thenReturn(adminRole);

        // Act
        ResponseEntity<Void> response = restRoleController.deleteRole(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteRole_Failure() {
        // Arrange
        when(roleService.getRole(1L)).thenThrow(new InvalidOperationException("No se puede eliminar un rol en uso"));

        // Act
        ResponseEntity<Void> response = restRoleController.deleteRole(1L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteRole_NotFound() {
        // Arrange
        when(roleService.getRole(1L)).thenThrow(new ResourceNotFoundException("Rol", "id", 1L));

        // Act
        ResponseEntity<Void> response = restRoleController.deleteRole(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteRole_InternalServerError_ReturnsError() {
        // Arrange
        when(roleService.getRole(1L)).thenReturn(adminRole);
        doThrow(new RuntimeException("Error interno del servidor"))
            .when(roleService).deleteRole(any(Role.class));

        // Act
        ResponseEntity<Void> response = restRoleController.deleteRole(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testAssignPermissionToRole_Success() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;
        RoleWithPermissionsDTO expectedRole = new RoleWithPermissionsDTO(1L, "Admin", new ArrayList<>());
        when(permissionRepository.existsById(permissionId)).thenReturn(true);
        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)).thenReturn(false);
        when(roleService.getRole(roleId)).thenReturn(adminRole);
        when(roleMapper.toRoleWithPermissionsDTO(adminRole)).thenReturn(expectedRole);

        // Act
        ResponseEntity<RoleWithPermissionsDTO> response = 
            restRoleController.assignPermissionToRole(roleId, permissionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RoleWithPermissionsDTO role = response.getBody();
        assertNotNull(role);
        assertEquals(1L, role.getRoleId());
        assertEquals("Admin", role.getRoleName());
    }

    @Test
    void assignPermissionToRole_PermissionNotFound_ReturnsNotFound() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;
        when(roleService.getRole(roleId)).thenReturn(adminRole);
        doThrow(new ResourceNotFoundException("Permiso", "id", permissionId))
            .when(rolePermissionService).assignPermissionToRole(permissionId, roleId);

        // Act
        ResponseEntity<RoleWithPermissionsDTO> response = 
            restRoleController.assignPermissionToRole(roleId, permissionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void assignPermissionToRole_InvalidOperationException_ReturnsError() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;
        when(roleService.getRole(roleId)).thenReturn(adminRole);
        doThrow(new InvalidOperationException("No se puede asignar un permiso a un rol en uso"))
            .when(rolePermissionService).assignPermissionToRole(permissionId, roleId);

        // Act
        ResponseEntity<RoleWithPermissionsDTO> response = 
            restRoleController.assignPermissionToRole(roleId, permissionId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void assignPermissionToRole_InternalServerError_ReturnsError() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;
        when(roleService.getRole(roleId)).thenReturn(adminRole);
        doThrow(new RuntimeException("Error interno del servidor"))
            .when(rolePermissionService).assignPermissionToRole(permissionId, roleId);

        // Act
        ResponseEntity<RoleWithPermissionsDTO> response = 
            restRoleController.assignPermissionToRole(roleId, permissionId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testRemovePermissionFromRole_Success() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;
        RoleWithPermissionsDTO expectedRole = new RoleWithPermissionsDTO(1L, "Admin", new ArrayList<>());
        when(roleService.getRole(roleId)).thenReturn(adminRole);
        when(roleMapper.toRoleWithPermissionsDTO(adminRole)).thenReturn(expectedRole);

        // Act
        ResponseEntity<RoleWithPermissionsDTO> response = 
            restRoleController.removePermissionFromRole(roleId, permissionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RoleWithPermissionsDTO role = response.getBody();
        assertNotNull(role);
        assertEquals(1L, role.getRoleId());
        assertEquals("Admin", role.getRoleName());
    }

    @Test
    void testRemovePermissionFromRole_Failure() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;
        when(roleService.getRole(roleId)).thenThrow(new ResourceNotFoundException("Rol", "id", roleId));

        // Act
        ResponseEntity<RoleWithPermissionsDTO> response = 
            restRoleController.removePermissionFromRole(roleId, permissionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void removePermissionFromRole_PermissionNotFound_ReturnsNotFound() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;
        when(roleService.getRole(roleId)).thenReturn(adminRole);
        doThrow(new ResourceNotFoundException("Permiso", "id", permissionId))
            .when(rolePermissionService).removePermissionFromRole(permissionId, roleId);

        // Act
        ResponseEntity<RoleWithPermissionsDTO> response = 
            restRoleController.removePermissionFromRole(roleId, permissionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void removePermissionFromRole_InvalidOperationException_ReturnsError() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;
        when(roleService.getRole(roleId)).thenReturn(adminRole);
        doThrow(new InvalidOperationException("No se puede eliminar un permiso de un rol en uso"))
            .when(rolePermissionService).removePermissionFromRole(permissionId, roleId);

        // Act
        ResponseEntity<RoleWithPermissionsDTO> response = 
            restRoleController.removePermissionFromRole(roleId, permissionId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void removePermissionFromRole_InternalServerError_ReturnsError() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;
        when(roleService.getRole(roleId)).thenReturn(adminRole);
        doThrow(new RuntimeException("Error interno del servidor"))
            .when(rolePermissionService).removePermissionFromRole(permissionId, roleId);

        // Act
        ResponseEntity<RoleWithPermissionsDTO> response = 
            restRoleController.removePermissionFromRole(roleId, permissionId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    
}