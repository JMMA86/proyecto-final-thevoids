package org.thevoids.oncologic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thevoids.oncologic.repository.PermissionRepository;
import org.thevoids.oncologic.repository.RolePermissionRepository;
import org.thevoids.oncologic.repository.RoleRepository;
import org.thevoids.oncologic.service.impl.RolePermissionServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RolePermissionServiceUnitTest {

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private RolePermissionServiceImpl rolePermissionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void roleHasPermission_WhenRoleAndPermissionExist_ReturnsTrue() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(permissionRepository.existsById(permissionId)).thenReturn(true);
        when(rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)).thenReturn(true);

        // Act
        boolean result = rolePermissionService.roleHasPermission(roleId, permissionId);

        // Assert
        assertTrue(result);
    }

    @Test
    void roleHasPermission_WhenRoleAndPermissionExist_ReturnsFalse() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(permissionRepository.existsById(permissionId)).thenReturn(true);
        when(rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)).thenReturn(false);

        // Act
        boolean result = rolePermissionService.roleHasPermission(roleId, permissionId);

        // Assert
        assertFalse(result);
    }

    @Test
    void roleHasPermission_WhenRoleNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        when(roleRepository.existsById(roleId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rolePermissionService.roleHasPermission(roleId, permissionId));
    }

    @Test
    void roleHasPermission_WhenPermissionNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(permissionRepository.existsById(permissionId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rolePermissionService.roleHasPermission(roleId, permissionId));
    }
}