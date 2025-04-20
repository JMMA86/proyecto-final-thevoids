package org.thevoids.oncologic.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.impl.UserServiceImpl;

class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AssignedRoles assignedRolesService;

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
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void createUser_WhenCalled_SavesUserAndAssignsRole() {
        // Arrange
        User user = new User();
        user.setIdentification("123456");
        user.setPassword("password");

        when(userRepository.findByIdentification("123456")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User createdUser = userService.createUser(user);

        // Assert
        assertNotNull(createdUser);
        assertEquals("encodedPassword", createdUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_WhenUserExists_ThrowsException() {
        // Arrange
        User existingUser = new User();
        existingUser.setIdentification("123456");

        when(userRepository.findByIdentification("123456")).thenReturn(Optional.of(existingUser));

        User newUser = new User();
        newUser.setIdentification("123456");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(newUser);
        });

        assertEquals("User with identification 123456 already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(user);
        });

        assertEquals("User with id 1 does not exist", exception.getMessage());
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
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }

    @Test
    void getUserById_WhenUserNotFound_ReturnsNull() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNull(result);
    }

    @Test
    void updateUser_WhenCalled_UpdatesUser() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        when(userRepository.existsById(user.getUserId())).thenReturn(true);

        // Act
        userService.updateUser(user);

        // Assert
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_WhenUserNotFound_ThrowsException() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        when(userRepository.existsById(user.getUserId())).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(user);
        });

        assertEquals("User with id 1 does not exist", exception.getMessage());
    }
}