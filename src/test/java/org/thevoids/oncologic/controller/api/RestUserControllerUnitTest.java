package org.thevoids.oncologic.controller.api;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.custom.ApiResponse;
import org.thevoids.oncologic.dto.entity.UserDTO;
import org.thevoids.oncologic.dto.entity.UserWithRolesDTO;
import org.thevoids.oncologic.entity.AssignedRole;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.mapper.UserMapper;
import org.thevoids.oncologic.service.UserService;
import org.thevoids.oncologic.service.AssignedRoles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestUserControllerUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AssignedRoles assignedRolesService;

    @InjectMocks
    private RestUserController restUserController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ReturnsUserList() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        user.setFullName("John Doe");

        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("Admin");

        AssignedRole assignedRole = new AssignedRole();
        assignedRole.setUser(user);
        assignedRole.setRole(role);
        user.setAssignedRoles(List.of(assignedRole));

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setFullName("John Doe");

        when(userService.getAllUsers()).thenReturn(List.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        // Act
        ResponseEntity<ApiResponse<List<UserDTO>>> response = restUserController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<List<UserDTO>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Usuarios recuperados con éxito", responseBody.getMensaje());
        assertTrue(responseBody.isExito());
        
        List<UserDTO> users = responseBody.getDatos();
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getFullName());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getAllUsers_ReturnsError() {
        // Arrange
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Error al recuperar los usuarios"));

        // Act
        ResponseEntity<ApiResponse<List<UserDTO>>> response = restUserController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ApiResponse<List<UserDTO>> errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Error al recuperar los usuarios: Error al recuperar los usuarios", errorResponse.getMensaje());
        assertEquals(false, errorResponse.isExito());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void createUser_SuccessfulCreation_ReturnsUser() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        user.setFullName("John Doe");

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setFullName("John Doe");
        
        when(userMapper.toUser(any(UserDTO.class))).thenReturn(user);
        when(userService.createUser(any(User.class))).thenReturn(user);
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = restUserController.createUser(userDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ApiResponse<UserDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Usuario creado con éxito", responseBody.getMensaje());
        assertTrue(responseBody.isExito());
        
        UserDTO createdUser = responseBody.getDatos();
        assertEquals(1L, createdUser.getUserId());
        assertEquals("John Doe", createdUser.getFullName());
        verify(userService, times(1)).createUser(any(User.class));
    }
    
    @Test
    void createUser_FailedCreation_ReturnsError() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName("John Doe");
        
        when(userMapper.toUser(any(UserDTO.class))).thenReturn(new User());
        doThrow(new IllegalArgumentException("Error al crear el usuario")).when(userService).createUser(any(User.class));
    
        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = restUserController.createUser(userDTO);
    
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<UserDTO> errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Error al crear el usuario: Error al crear el usuario", errorResponse.getMensaje());
        assertEquals(false, errorResponse.isExito());
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void getUserById_UserExists_ReturnsUser() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setFullName("John Doe");

        UserWithRolesDTO userDTO = new UserWithRolesDTO();
        userDTO.setUserId(userId);
        userDTO.setFullName("John Doe");

        when(userService.getUserById(userId)).thenReturn(user);
        when(userMapper.toUserWithRolesDTO(user)).thenReturn(userDTO);

        // Act
        ResponseEntity<ApiResponse<UserWithRolesDTO>> response = restUserController.getUserById(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<UserWithRolesDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Usuario recuperado con éxito", responseBody.getMensaje());
        assertTrue(responseBody.isExito());
        
        UserWithRolesDTO retrievedUser = responseBody.getDatos();
        assertEquals(userId, retrievedUser.getUserId());
        assertEquals("John Doe", retrievedUser.getFullName());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUserById_UserNotFound_ReturnsError() {
        // Arrange
        Long userId = 1L;
        when(userService.getUserById(userId)).thenThrow(new IllegalArgumentException("User with id " + userId + " does not exist"));

        // Act
        ResponseEntity<ApiResponse<UserWithRolesDTO>> response = restUserController.getUserById(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ApiResponse<UserWithRolesDTO> errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Error al recuperar el usuario: User with id " + userId + " does not exist", errorResponse.getMensaje());
        assertEquals(false, errorResponse.isExito());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void updateUser_UserExists_ReturnsUpdatedUser() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setFullName("John Doe");

        UserDTO userDTO = new UserDTO();
        userDTO.setFullName("John Updated");
        userDTO.setEmail("john@example.com");

        User updatedUser = new User();
        updatedUser.setUserId(userId);
        updatedUser.setFullName("John Updated");
        updatedUser.setEmail("john@example.com");

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setUserId(userId);
        updatedUserDTO.setFullName("John Updated");
        updatedUserDTO.setEmail("john@example.com");

        when(userService.getUserById(userId)).thenReturn(existingUser, updatedUser);
        doNothing().when(userService).updateUser(any(User.class));
        when(userMapper.toUserDTO(updatedUser)).thenReturn(updatedUserDTO);

        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = restUserController.updateUser(userId, userDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<UserDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Usuario actualizado con éxito", responseBody.getMensaje());
        assertTrue(responseBody.isExito());
        
        UserDTO updatedUserResult = responseBody.getDatos();
        assertEquals(userId, updatedUserResult.getUserId());
        assertEquals("John Updated", updatedUserResult.getFullName());
        assertEquals("john@example.com", updatedUserResult.getEmail());
        verify(userService, times(1)).updateUser(any(User.class));
        verify(userService, times(2)).getUserById(userId);
    }

    @Test
    void updateUser_UserNotFound_ReturnsError() {
        // Arrange
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName("John Updated");
        
        when(userService.getUserById(userId)).thenReturn(null);

        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = restUserController.updateUser(userId, userDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<UserDTO> errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Error al actualizar el usuario: Usuario no encontrado", errorResponse.getMensaje());
        assertEquals(false, errorResponse.isExito());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void deleteUser_UserExists_ReturnsSuccess() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setFullName("John Doe");

        when(userService.getUserById(userId)).thenReturn(user);
        doNothing().when(userService).deleteUser(user);

        // Act
        ResponseEntity<ApiResponse<Void>> response = restUserController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<Void> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Usuario eliminado con éxito", responseBody.getMensaje());
        assertTrue(responseBody.isExito());
        verify(userService, times(1)).getUserById(userId);
        verify(userService, times(1)).deleteUser(user);
    }

    @Test
    void deleteUser_UserNotFound_ReturnsError() {
        // Arrange
        Long userId = 1L;
        when(userService.getUserById(userId)).thenThrow(new IllegalArgumentException("User with id " + userId + " does not exist"));

        // Act
        ResponseEntity<ApiResponse<Void>> response = restUserController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<Void> errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Error al eliminar el usuario: User with id " + userId + " does not exist", errorResponse.getMensaje());
        assertEquals(false, errorResponse.isExito());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void assignRoleToUser_Success() {
        // Arrange
        Long userId = 1L;
        Long roleId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setFullName("John Doe");

        UserWithRolesDTO userWithRolesDTO = new UserWithRolesDTO();
        userWithRolesDTO.setUserId(userId);
        userWithRolesDTO.setFullName("John Doe");

        when(userService.getUserById(userId)).thenReturn(user);
        when(userMapper.toUserWithRolesDTO(user)).thenReturn(userWithRolesDTO);
        doNothing().when(assignedRolesService).assignRoleToUser(roleId, userId);

        // Act
        ResponseEntity<ApiResponse<UserWithRolesDTO>> response = restUserController.assignRoleToUser(userId, roleId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<UserWithRolesDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Rol asignado al usuario con éxito", responseBody.getMensaje());
        assertTrue(responseBody.isExito());
        
        UserWithRolesDTO updatedUser = responseBody.getDatos();
        assertEquals(userId, updatedUser.getUserId());
        assertEquals("John Doe", updatedUser.getFullName());
        verify(assignedRolesService, times(1)).assignRoleToUser(roleId, userId);
    }

    @Test
    void assignRoleToUser_Failure() {
        // Arrange
        Long userId = 1L;
        Long roleId = 1L;
        when(userService.getUserById(userId)).thenThrow(new RuntimeException("Error al asignar rol"));

        // Act
        ResponseEntity<ApiResponse<UserWithRolesDTO>> response = restUserController.assignRoleToUser(userId, roleId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<UserWithRolesDTO> errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Error al añadir rol al usuario: Error al asignar rol", errorResponse.getMensaje());
        assertEquals(false, errorResponse.isExito());
    }

    @Test
    void removeRoleFromUser_Success() {
        // Arrange
        Long userId = 1L;
        Long roleId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setFullName("John Doe");

        UserWithRolesDTO userWithRolesDTO = new UserWithRolesDTO();
        userWithRolesDTO.setUserId(userId);
        userWithRolesDTO.setFullName("John Doe");

        when(userService.getUserById(userId)).thenReturn(user);
        when(userMapper.toUserWithRolesDTO(user)).thenReturn(userWithRolesDTO);
        doNothing().when(assignedRolesService).removeRoleFromUser(roleId, userId);

        // Act
        ResponseEntity<ApiResponse<UserWithRolesDTO>> response = restUserController.removeRoleFromUser(userId, roleId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<UserWithRolesDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Rol eliminado del usuario con éxito", responseBody.getMensaje());
        assertTrue(responseBody.isExito());
        
        UserWithRolesDTO updatedUser = responseBody.getDatos();
        assertEquals(userId, updatedUser.getUserId());
        assertEquals("John Doe", updatedUser.getFullName());
        verify(assignedRolesService, times(1)).removeRoleFromUser(roleId, userId);
    }

    @Test
    void removeRoleFromUser_Failure() {
        // Arrange
        Long userId = 1L;
        Long roleId = 1L;
        when(userService.getUserById(userId)).thenThrow(new RuntimeException("Error al eliminar rol"));

        // Act
        ResponseEntity<ApiResponse<UserWithRolesDTO>> response = restUserController.removeRoleFromUser(userId, roleId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<UserWithRolesDTO> errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Error al eliminar rol del usuario: Error al eliminar rol", errorResponse.getMensaje());
        assertEquals(false, errorResponse.isExito());
    }
}
