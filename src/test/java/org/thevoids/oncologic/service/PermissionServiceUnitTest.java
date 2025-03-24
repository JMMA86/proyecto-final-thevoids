package org.thevoids.oncologic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.repository.PermissionRepository;
import org.thevoids.oncologic.service.impl.PermissionServiceImpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
}