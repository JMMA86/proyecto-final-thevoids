package org.thevoids.oncologic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.RolePermission;
import org.thevoids.oncologic.repository.RoleRepository;
import org.thevoids.oncologic.service.impl.RoleServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
        assertThrows(IllegalArgumentException.class, () -> roleService.createRole(role));
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
        assertThrows(IllegalArgumentException.class, () -> roleService.deleteRole(role));
    }

    @Test
    void createRole_WhenRoleHasNoPermissions_ThrowsException() {
        // Arrange
        Role role = new Role();
        role.setRoleId(1L);
        role.setRolePermissions(null); // or new ArrayList<>()

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> roleService.createRole(role));
    }
}