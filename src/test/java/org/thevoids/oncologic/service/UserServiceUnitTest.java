package org.thevoids.oncologic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_WhenCalled_ReturnsUserList() {
        // Arrange
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assert result.size() == 2;
    }

    @Test
    void createUser_WhenCalled_SavesUser() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        Role role = new Role();
        role.setRoleId(1L);
        user.setRole(role); // Agregar rol al usuario
        when(userRepository.existsById(user.getUserId())).thenReturn(false);

        // Act
        userService.createUser(user);

        // Assert
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_WhenUserExists_ThrowsException() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        when(userRepository.existsById(user.getUserId())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    @Test
    void deleteUser_WhenCalled_DeletesUser() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        when(userRepository.existsById(user.getUserId())).thenReturn(true);

        // Act
        userService.deleteUser(user);

        // Assert
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_WhenUserNotFound_ThrowsException() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        when(userRepository.existsById(user.getUserId())).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(user));
    }

    @Test
    void getUserById_WhenCalled_ReturnsUser() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assert result.getUserId().equals(userId);
    }

    @Test
    void getUserById_WhenUserNotFound_ReturnsNull() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assert result == null;
    }

    @Test
    void createUser_WhenUserHasNoRole_ThrowsException() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        user.setRole(null); // Usuario sin rol

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }
}