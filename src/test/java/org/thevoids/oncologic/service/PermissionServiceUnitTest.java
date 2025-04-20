package org.thevoids.oncologic.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.repository.PermissionRepository;
import org.thevoids.oncologic.service.impl.PermissionServiceImpl;

class PermissionServiceUnitTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPermissions_WhenCalled_ReturnsListOfPermissions() {
        // Arrange
        Permission permission1 = new Permission();
        permission1.setPermissionId(1L);
        Permission permission2 = new Permission();
        permission2.setPermissionId(2L);
        when(permissionRepository.findAll()).thenReturn(List.of(permission1, permission2));

        // Act
        List<Permission> result = permissionService.getAllPermissions();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void createPermission_WhenCalled_SavesPermission() {
        // Arrange
        Permission permission = new Permission();
        permission.setPermissionId(1L);
        when(permissionRepository.existsById(permission.getPermissionId())).thenReturn(false);

        // Act
        permissionService.createPermission(permission);

        // Assert
        verify(permissionRepository, times(1)).save(permission);
    }

    @Test
    void createPermission_WhenPermissionExists_ThrowsException() {
        // Arrange
        Permission permission = new Permission();
        permission.setPermissionId(1L);
        when(permissionRepository.existsById(permission.getPermissionId())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> permissionService.createPermission(permission));
    }

    @Test
    void createPermission_WhenPermissionWithSameIdExists_ThrowsException() {
        // Arrange
        Permission existingPermission = new Permission();
        existingPermission.setPermissionId(1L);
        existingPermission.setPermissionName("READ");

        Permission newPermission = new Permission();
        newPermission.setPermissionId(1L);
        newPermission.setPermissionName("WRITE");

        when(permissionRepository.existsById(newPermission.getPermissionId())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            permissionService.createPermission(newPermission);
        });

        assertEquals("Permission already exists", exception.getMessage());
        verify(permissionRepository, never()).save(any(Permission.class));
    }

    @Test
    void deletePermission_WhenCalled_DeletesPermission() {
        // Arrange
        Long permissionId = 1L;
        when(permissionRepository.existsById(permissionId)).thenReturn(true);

        // Act
        permissionService.deletePermission(permissionId);

        // Assert
        verify(permissionRepository, times(1)).deleteById(permissionId);
    }

    @Test
    void deletePermission_WhenPermissionNotFound_ThrowsException() {
        // Arrange
        Long permissionId = 1L;
        when(permissionRepository.existsById(permissionId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> permissionService.deletePermission(permissionId));
    }

    @Test
    void updatePermission_WhenCalled_UpdatesPermission() {
        // Arrange
        Permission permission = new Permission();
        permission.setPermissionId(1L);
        when(permissionRepository.existsById(permission.getPermissionId())).thenReturn(true);

        // Act
        permissionService.updatePermission(permission);

        // Assert
        verify(permissionRepository, times(1)).save(permission);
    }

    @Test
    void updatePermission_WhenPermissionNotFound_ThrowsException() {
        // Arrange
        Permission permission = new Permission();
        permission.setPermissionId(1L);
        when(permissionRepository.existsById(permission.getPermissionId())).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> permissionService.updatePermission(permission));
    }

    @Test
    void getPermission_WhenCalled_ReturnsPermission() {
        // Arrange
        Permission permission = new Permission();
        permission.setPermissionId(1L);
        when(permissionRepository.existsById(permission.getPermissionId())).thenReturn(true);
        when(permissionRepository.findById(permission.getPermissionId())).thenReturn(Optional.of(permission));

        // Act
        Optional<Permission> result = permissionService.getPermission(permission.getPermissionId());

        // Assert
        assertEquals(permission, result.orElse(null));
    }

    @Test
    void getPermission_WhenPermissionNotFound_ThrowsException() {
        // Arrange
        Long permissionId = 1L;
        when(permissionRepository.existsById(permissionId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> permissionService.getPermission(permissionId));
    }
}