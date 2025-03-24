package org.thevoids.oncologic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thevoids.oncologic.entity.AssignedRole;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.repository.AssignedRoleRepository;
import org.thevoids.oncologic.repository.RoleRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.impl.AssignedRolesImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AssignedRolesServiceUnitTest {

    @Mock
    private AssignedRoleRepository assignedRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AssignedRolesImpl assignedRoles;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void assignRoleToUser_WhenCalled_SavesAssignedRole() {
        // Arrange
        Long roleId = 1L;
        Long userId = 1L;

        Role role = new Role();
        role.setRoleId(roleId);

        User user = new User();
        user.setUserId(userId);

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(assignedRoleRepository.existsByRoleIdAndUserId(roleId, userId)).thenReturn(false);

        // Act
        assignedRoles.assignRoleToUser(roleId, userId);

        // Assert
        verify(assignedRoleRepository, times(1)).save(any(AssignedRole.class));
    }

    @Test
    void removeRoleFromUser_WhenCalled_DeletesAssignedRole() {
        // Arrange
        Long roleId = 1L;
        Long userId = 1L;

        Role role = new Role();
        role.setRoleId(roleId);

        User user = new User();
        user.setUserId(userId);

        AssignedRole assignedRole = new AssignedRole();
        assignedRole.setId(1L);
        assignedRole.setRole(role);
        assignedRole.setUser(user);

        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(userRepository.existsById(userId)).thenReturn(true);
        when(assignedRoleRepository.existsByRoleIdAndUserId(roleId, userId)).thenReturn(true);
        when(assignedRoleRepository.findByRoleIdAndUserId(roleId, userId)).thenReturn(Optional.of(assignedRole));

        // Act
        assignedRoles.removeRoleFromUser(roleId, userId);

        // Assert
        verify(assignedRoleRepository, times(1)).deleteById(assignedRole.getId());
    }

    @Test
    void assignRoleToUser_WhenRoleNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long userId = 1L;

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> assignedRoles.assignRoleToUser(roleId, userId));
    }

    @Test
    void assignRoleToUser_WhenUserNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> assignedRoles.assignRoleToUser(roleId, userId));
    }

    @Test
    void removeRoleFromUser_WhenRoleNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long userId = 1L;

        when(roleRepository.existsById(roleId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> assignedRoles.removeRoleFromUser(roleId, userId));
    }

    @Test
    void removeRoleFromUser_WhenUserNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> assignedRoles.removeRoleFromUser(roleId, userId));
    }

    @Test
    void removeRoleFromUser_WhenAssignedRoleNotFound_ThrowsException() {
        // Arrange
        Long roleId = 1L;
        Long userId = 1L;

        when(assignedRoleRepository.existsByRoleIdAndUserId(roleId, userId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> assignedRoles.removeRoleFromUser(roleId, userId));
    }
}