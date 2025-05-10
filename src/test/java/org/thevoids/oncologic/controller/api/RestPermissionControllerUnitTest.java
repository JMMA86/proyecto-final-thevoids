package org.thevoids.oncologic.controller.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.entity.PermissionDTO;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.exception.ResourceAlreadyExistsException;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.mapper.PermissionMapper;
import org.thevoids.oncologic.service.PermissionService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestPermissionControllerUnitTest {

    @Mock
    private PermissionService permissionService;

    @Mock
    private PermissionMapper permissionMapper;

    @InjectMocks
    private RestPermissionController restPermissionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPermissions_ReturnsPermissionList() {
        // Arrange
        Permission permission1 = new Permission();
        permission1.setPermissionId(1L);
        permission1.setPermissionName("VIEW_USERS");

        Permission permission2 = new Permission();
        permission2.setPermissionId(2L);
        permission2.setPermissionName("EDIT_USERS");

        List<Permission> permissions = Arrays.asList(permission1, permission2);

        PermissionDTO permissionDTO1 = new PermissionDTO();
        permissionDTO1.setPermissionId(1L);
        permissionDTO1.setPermissionName("VIEW_USERS");

        PermissionDTO permissionDTO2 = new PermissionDTO();
        permissionDTO2.setPermissionId(2L);
        permissionDTO2.setPermissionName("EDIT_USERS");

        when(permissionService.getAllPermissions()).thenReturn(permissions);
        when(permissionMapper.toPermissionDTO(permission1)).thenReturn(permissionDTO1);
        when(permissionMapper.toPermissionDTO(permission2)).thenReturn(permissionDTO2);

        // Act
        ResponseEntity<List<PermissionDTO>> response = restPermissionController.getAllPermissions();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<PermissionDTO> permissionDTOs = response.getBody();
        assertNotNull(permissionDTOs);
        assertEquals(2, permissionDTOs.size());
        assertEquals("VIEW_USERS", permissionDTOs.get(0).getPermissionName());
        assertEquals("EDIT_USERS", permissionDTOs.get(1).getPermissionName());
        
        verify(permissionService, times(1)).getAllPermissions();
    }

    @Test
    void getAllPermissions_ReturnsError() {
        // Arrange
        when(permissionService.getAllPermissions()).thenThrow(new RuntimeException("Error al recuperar permisos"));

        // Act
        ResponseEntity<List<PermissionDTO>> response = restPermissionController.getAllPermissions();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(permissionService, times(1)).getAllPermissions();
    }

    @Test
    void getPermissionById_PermissionExists_ReturnsPermission() {
        // Arrange
        Long permissionId = 1L;
        Permission permission = new Permission();
        permission.setPermissionId(permissionId);
        permission.setPermissionName("VIEW_USERS");

        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionId(permissionId);
        permissionDTO.setPermissionName("VIEW_USERS");

        when(permissionService.getPermission(permissionId)).thenReturn(Optional.of(permission));
        when(permissionMapper.toPermissionDTO(permission)).thenReturn(permissionDTO);

        // Act
        ResponseEntity<PermissionDTO> response = restPermissionController.getPermissionById(permissionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PermissionDTO retrievedPermission = response.getBody();
        assertNotNull(retrievedPermission);
        assertEquals(permissionId, retrievedPermission.getPermissionId());
        assertEquals("VIEW_USERS", retrievedPermission.getPermissionName());
        
        verify(permissionService, times(1)).getPermission(permissionId);
    }

    @Test
    void getPermissionById_PermissionNotFound_ReturnsError() {
        // Arrange
        Long permissionId = 1L;
        when(permissionService.getPermission(permissionId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<PermissionDTO> response = restPermissionController.getPermissionById(permissionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(permissionService, times(1)).getPermission(permissionId);
    }

    @Test
    void createPermission_SuccessfulCreation_ReturnsPermission() {
        // Arrange
        Permission permission = new Permission();
        permission.setPermissionName("VIEW_USERS");

        Permission createdPermission = new Permission();
        createdPermission.setPermissionId(1L);
        createdPermission.setPermissionName("VIEW_USERS");

        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionName("VIEW_USERS");

        PermissionDTO createdPermissionDTO = new PermissionDTO();
        createdPermissionDTO.setPermissionId(1L);
        createdPermissionDTO.setPermissionName("VIEW_USERS");

        when(permissionMapper.toPermission(any(PermissionDTO.class))).thenReturn(permission);
        when(permissionService.createPermission(any(Permission.class))).thenReturn(createdPermission);
        when(permissionMapper.toPermissionDTO(createdPermission)).thenReturn(createdPermissionDTO);

        // Act
        ResponseEntity<PermissionDTO> response = restPermissionController.createPermission(permissionDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        PermissionDTO createdPermissionResult = response.getBody();
        assertNotNull(createdPermissionResult);
        assertEquals(1L, createdPermissionResult.getPermissionId());
        assertEquals("VIEW_USERS", createdPermissionResult.getPermissionName());
        
        verify(permissionService, times(1)).createPermission(any(Permission.class));
    }

    @Test
    void createPermission_FailedCreation_ReturnsError() {
        // Arrange
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionName("VIEW_USERS");

        when(permissionMapper.toPermission(any(PermissionDTO.class))).thenReturn(new Permission());
        when(permissionService.createPermission(any(Permission.class)))
            .thenThrow(new ResourceAlreadyExistsException("Permiso", "nombre", "VIEW_USERS"));

        // Act
        ResponseEntity<PermissionDTO> response = restPermissionController.createPermission(permissionDTO);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(permissionService, times(1)).createPermission(any(Permission.class));
    }

    @Test
    void updatePermission_PermissionExists_ReturnsUpdatedPermission() {
        // Arrange
        Long permissionId = 1L;
        
        Permission existingPermission = new Permission();
        existingPermission.setPermissionId(permissionId);
        existingPermission.setPermissionName("VIEW_USERS");
        
        Permission updatedPermission = new Permission();
        updatedPermission.setPermissionId(permissionId);
        updatedPermission.setPermissionName("VIEW_USERS_UPDATED");
        
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionName("VIEW_USERS_UPDATED");
        
        PermissionDTO updatedPermissionDTO = new PermissionDTO();
        updatedPermissionDTO.setPermissionId(permissionId);
        updatedPermissionDTO.setPermissionName("VIEW_USERS_UPDATED");
        
        when(permissionService.getPermission(permissionId)).thenReturn(Optional.of(existingPermission));
        when(permissionService.updatePermission(any(Permission.class))).thenReturn(updatedPermission);
        when(permissionMapper.toPermissionDTO(updatedPermission)).thenReturn(updatedPermissionDTO);
        
        // Act
        ResponseEntity<PermissionDTO> response = restPermissionController.updatePermission(permissionId, permissionDTO);
                
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PermissionDTO updatedPermissionResult = response.getBody();
        assertNotNull(updatedPermissionResult);
        assertEquals(permissionId, updatedPermissionResult.getPermissionId());
        assertEquals("VIEW_USERS_UPDATED", updatedPermissionResult.getPermissionName());
              
        verify(permissionService, times(1)).getPermission(permissionId);
        verify(permissionService, times(1)).updatePermission(any(Permission.class));
    }
    
    @Test
    void updatePermission_PermissionNotFound_ReturnsError() {
        // Arrange
        Long permissionId = 1L;
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionName("VIEW_USERS_UPDATED");
        
        when(permissionService.getPermission(permissionId)).thenReturn(Optional.empty());
        
        // Act
        ResponseEntity<PermissionDTO> response = restPermissionController.updatePermission(permissionId, permissionDTO);
                
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(permissionService, times(1)).getPermission(permissionId);
        verify(permissionService, times(0)).updatePermission(any(Permission.class));
    }
    
    @Test
    void deletePermission_SuccessfulDeletion_ReturnsSuccess() {
        // Arrange
        Long permissionId = 1L;
        
        // Act
        ResponseEntity<Void> response = restPermissionController.deletePermission(permissionId);
                
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(permissionService, times(1)).deletePermission(permissionId);
    }
    
    @Test
    void deletePermission_FailedDeletion_ReturnsError() {
        // Arrange
        Long permissionId = 1L;
        when(permissionService.deletePermission(permissionId))
            .thenThrow(new InvalidOperationException("No se puede eliminar un permiso en uso"));
        
        // Act
        ResponseEntity<Void> response = restPermissionController.deletePermission(permissionId);
                
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(permissionService, times(1)).deletePermission(permissionId);
    }
}
