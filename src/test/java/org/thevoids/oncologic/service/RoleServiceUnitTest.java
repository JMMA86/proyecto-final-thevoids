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
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.RolePermission;
import org.thevoids.oncologic.repository.RoleRepository;
import org.thevoids.oncologic.service.impl.RoleServiceImpl;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.ResourceAlreadyExistsException;

class RoleServiceUnitTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRoles_WhenCalled_ReturnsRoleList() {
        // Arrange
        List<Role> roles = List.of(new Role(), new Role());
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        List<Role> result = roleService.getAllRoles();

        // Assert
        assert result.size() == 2;
    }

    @Test
    void createRole_WhenCalled_SavesRole() {
        // Arrange
        Role role = new Role();
        role.setRoleId(1L);
        role.setRolePermissions(List.of(new RolePermission())); // Agregar permisos al rol
        when(roleRepository.existsById(role.getRoleId())).thenReturn(false);

        // Act
        roleService.createRole(role);

        // Assert
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void createRole_WhenRoleExists_ThrowsException() {
        // Arrange
        Role role = new Role();
        role.setRoleId(1L);
        when(roleRepository.existsById(role.getRoleId())).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> roleService.createRole(role));
    }

    @Test
    void createRole_WhenRoleWithSameIdExists_ThrowsException() {
        // Arrange
        Role existingRole = new Role();
        existingRole.setRoleId(1L); // Existing ID
        existingRole.setRoleName("Admin");

        Role newRole = new Role();
        newRole.setRoleId(1L); // Same ID as existing role
        newRole.setRoleName("User");

        when(roleRepository.existsById(newRole.getRoleId())).thenReturn(true);

        // Act & Assert
        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            roleService.createRole(newRole);
        });

        assertEquals("Rol ya existe con id : '1'", exception.getMessage());
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void deleteRole_WhenCalled_DeletesRole() {
        // Arrange
        Role role = new Role();
        role.setRoleId(1L);
        when(roleRepository.existsById(role.getRoleId())).thenReturn(true);

        // Act
        roleService.deleteRole(role);

        // Assert
        verify(roleRepository, times(1)).delete(role);
    }

    @Test
    void deleteRole_WhenRoleNotFound_ThrowsException() {
        // Arrange
        Role role = new Role();
        role.setRoleId(1L);
        when(roleRepository.existsById(role.getRoleId())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> roleService.deleteRole(role));
    }

    @Test
    void updateRole_WhenCalled_UpdatesRole() {
        // Arrange
        Role role = new Role();
        role.setRoleId(1L);
        role.setRolePermissions(List.of(new RolePermission())); // Agregar permisos al rol
        when(roleRepository.existsById(role.getRoleId())).thenReturn(true);

        // Act
        roleService.updateRole(role);

        // Assert
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void updateRole_WhenRoleNotFound_ThrowsException() {
        // Arrange
        Role role = new Role();
        role.setRoleId(1L);
        when(roleRepository.existsById(role.getRoleId())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> roleService.updateRole(role));
    }

    @Test
    void getRole_WhenCalled_ReturnsRole() {
        // Arrange
        Role role = new Role();
        role.setRoleId(1L);
        when(roleRepository.findById(role.getRoleId())).thenReturn(Optional.of(role));

        // Act
        Role result = roleService.getRole(role.getRoleId());

        // Assert
        assertEquals(role, result);
    }

    @Test
    void getRole_WhenRoleNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> roleService.getRole(roleId));
    }
}