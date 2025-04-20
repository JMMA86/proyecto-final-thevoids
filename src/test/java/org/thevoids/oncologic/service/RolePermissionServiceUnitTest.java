package org.thevoids.oncologic.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.RolePermission;
import org.thevoids.oncologic.repository.PermissionRepository;
import org.thevoids.oncologic.repository.RolePermissionRepository;
import org.thevoids.oncologic.repository.RoleRepository;
import org.thevoids.oncologic.service.impl.RolePermissionServiceImpl;

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

    @Test
    void assignPermissionToRole_WhenRoleAndPermissionExist_AssignsPermissionToRole() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        Role role = new Role();
        role.setRoleId(roleId);
        Permission permission = new Permission();
        permission.setPermissionId(permissionId);

        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(permissionRepository.existsById(permissionId)).thenReturn(true);
        when(rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)).thenReturn(false);
        when(permissionRepository.findById(permissionId)).thenReturn(Optional.of(permission));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // Act
        rolePermissionService.assignPermissionToRole(permissionId, roleId);

        // Assert
        verify(rolePermissionRepository, times(1)).save(any(RolePermission.class));
    }

    @Test
    void assignPermissionToRole_WhenRoleNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        Permission permission = new Permission();
        permission.setPermissionId(permissionId);

        when(permissionRepository.existsById(permissionId)).thenReturn(true);
        when(roleRepository.existsById(roleId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rolePermissionService.assignPermissionToRole(permissionId, roleId));
    }

    @Test
    void assignPermissionToRole_WhenPermissionNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(permissionRepository.existsById(permissionId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rolePermissionService.assignPermissionToRole(permissionId, roleId));
    }

    @Test
    void assignPermissionToRole_WhenRoleAlreadyHasPermission_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        when(permissionRepository.existsById(permissionId)).thenReturn(true);
        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rolePermissionService.assignPermissionToRole(permissionId, roleId));
    }

    @Test
    void updatePermissionForRole_WhenRoleAndPermissionExist_UpdatesPermissionForRole() {
        // Arrange
        Long roleId = 1L;
        Long lastPermissionId = 1L;
        Long newPermissionId = 2L;
    
        Role role = new Role();
        role.setRoleId(roleId);
        Permission lastPermission = new Permission();
        lastPermission.setPermissionId(lastPermissionId);
        Permission newPermission = new Permission();
        newPermission.setPermissionId(newPermissionId);

        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole(role);
        rolePermission.setPermission(lastPermission);
    
        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(permissionRepository.existsById(lastPermissionId)).thenReturn(true);
        when(permissionRepository.existsById(newPermissionId)).thenReturn(true);
        when(rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, lastPermissionId)).thenReturn(true);
        when(rolePermissionRepository.findByRoleIdAndPermissionId(roleId, lastPermissionId)).thenReturn(Optional.of(rolePermission));
        when(permissionRepository.findById(newPermissionId)).thenReturn(Optional.of(newPermission));

        // Act
        rolePermissionService.updatePermissionForRole(lastPermissionId, newPermissionId, roleId);

        // Assert
        verify(rolePermissionRepository, times(1)).save(any(RolePermission.class));
    }

    @Test
    void updatePermissionForRole_WhenRoleNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long lastPermissionId = 1L;
        Long newPermissionId = 2L;

        when(roleRepository.existsById(roleId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rolePermissionService.updatePermissionForRole(lastPermissionId, newPermissionId, roleId));
    }

    @Test
    void updatePermissionForRole_WhenLastPermissionNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long lastPermissionId = 1L;
        Long newPermissionId = 2L;

        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(permissionRepository.existsById(lastPermissionId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rolePermissionService.updatePermissionForRole(lastPermissionId, newPermissionId, roleId));
    }

    @Test
    void updatePermissionForRole_WhenNewPermissionNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long lastPermissionId = 1L;
        Long newPermissionId = 2L;

        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(permissionRepository.existsById(lastPermissionId)).thenReturn(true);
        when(permissionRepository.existsById(newPermissionId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rolePermissionService.updatePermissionForRole(lastPermissionId, newPermissionId, roleId));
    }

    @Test
    void updatePermissionForRole_WhenRoleDoesNotHaveLastPermission_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long lastPermissionId = 1L;
        Long newPermissionId = 2L;

        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(permissionRepository.existsById(lastPermissionId)).thenReturn(true);
        when(permissionRepository.existsById(newPermissionId)).thenReturn(true);
        when(rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, lastPermissionId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rolePermissionService.updatePermissionForRole(lastPermissionId, newPermissionId, roleId));
    }

    @Test
    void removePermissionFromRole_WhenRoleAndPermissionExist_RemovesPermissionFromRole() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        RolePermission rolePermission = new RolePermission();
        rolePermission.setId(1L);

        when(permissionRepository.existsById(permissionId)).thenReturn(true);
        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)).thenReturn(true);
        when(rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId)).thenReturn(Optional.of(rolePermission));

        // Act
        rolePermissionService.removePermissionFromRole(permissionId, roleId);

        // Assert
        verify(rolePermissionRepository, times(1)).deleteById(rolePermission.getId());
    }

    @Test
    void removePermissionFromRole_WhenPermissionNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        when(permissionRepository.existsById(permissionId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rolePermissionService.removePermissionFromRole(permissionId, roleId));
    }

    @Test
    void removePermissionFromRole_WhenRoleNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        when(permissionRepository.existsById(permissionId)).thenReturn(true);
        when(roleRepository.existsById(roleId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rolePermissionService.removePermissionFromRole(permissionId, roleId));
    }

    @Test
    void removePermissionFromRole_WhenRoleDoesNotHavePermission_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        when(permissionRepository.existsById(permissionId)).thenReturn(true);
        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rolePermissionService.removePermissionFromRole(permissionId, roleId));
    }

    @Test
    void getPermissionsFromRole_WhenRoleExists_ReturnsPermissions() {
        // Arrange
        Long roleId = 1L;

        when(roleRepository.existsById(roleId)).thenReturn(true);

        // Act
        rolePermissionService.getPermissionsFromRole(roleId);

        // Assert
        verify(rolePermissionRepository, times(1)).getPermissionsFromRole(roleId);
    }

    @Test
    void getPermissionsFromRole_WhenRoleDoesNotExist_ThrowsException() {
        // Arrange
        Long roleId = 1L;

        when(roleRepository.existsById(roleId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rolePermissionService.getPermissionsFromRole(roleId));
    }

    @Test
    void getAllPermissions_WhenCalled_ReturnsAllPermissions() {
        // Arrange
        Permission permission1 = new Permission();
        permission1.setPermissionId(1L);
        permission1.setPermissionName("READ");

        Permission permission2 = new Permission();
        permission2.setPermissionId(2L);
        permission2.setPermissionName("WRITE");

        when(permissionRepository.findAll()).thenReturn(List.of(permission1, permission2));

        // Act
        List<Permission> permissions = rolePermissionService.getAllPermissions();

        // Assert
        assertNotNull(permissions);
        assertEquals(2, permissions.size());
        assertEquals("READ", permissions.get(0).getPermissionName());
        assertEquals("WRITE", permissions.get(1).getPermissionName());
        verify(permissionRepository, times(1)).findAll();
    }
}