package org.thevoids.oncologic.controller.api;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thevoids.oncologic.dto.entity.UserDTO;
import org.thevoids.oncologic.dto.entity.UserWithRolesDTO;
import org.thevoids.oncologic.entity.AssignedRole;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.mapper.UserMapper;
import org.thevoids.oncologic.service.UserService;

class RestUserControllerUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private RestUserController restUserController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restUserController).build();
    }

    @Test
    void getAllUsers_ReturnsUserList() throws Exception {
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
        var result = mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.exito").value(true))
              .andExpect(jsonPath("$.mensaje").value("Usuarios recuperados con éxito"))
              .andExpect(jsonPath("$.datos").exists());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getAllUsers_ReturnsError() throws Exception {
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Error al recuperar los usuarios"));

        var result = mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isInternalServerError())
              .andExpect(jsonPath("$.exito").value(false))
              .andExpect(jsonPath("$.mensaje").value("Error al recuperar los usuarios: Error al recuperar los usuarios"))
              .andExpect(jsonPath("$.datos").isEmpty());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void createUser_SuccessfulCreation_ReturnsUser() throws Exception {
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
        var result = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fullName\":\"John Doe\"}"));

        // Assert
        result.andExpect(status().isCreated())
              .andExpect(jsonPath("$.exito").value(true))
              .andExpect(jsonPath("$.mensaje").value("Usuario creado con éxito"))
              .andExpect(jsonPath("$.datos.userId").value(1))
              .andExpect(jsonPath("$.datos.fullName").value("John Doe"));
        verify(userService, times(1)).createUser(any(User.class));
    }
    
    @Test
    void createUser_FailedCreation_ReturnsError() throws Exception {
        // Arrange
        User user = new User();
        when(userMapper.toUser(any(UserDTO.class))).thenReturn(user);
        doThrow(new IllegalArgumentException("Error al crear el usuario")).when(userService).createUser(any(User.class));
    
        // Act
        var result = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fullName\":\"John Doe\"}"));
    
        // Assert
        result.andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.exito").value(false))
              .andExpect(jsonPath("$.mensaje").value("Error al crear el usuario: Error al crear el usuario"))
              .andExpect(jsonPath("$.datos").isEmpty());
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void getUserById_UserExists_ReturnsUser() throws Exception {
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
        var result = mockMvc.perform(get("/api/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.exito").value(true))
              .andExpect(jsonPath("$.mensaje").value("Usuario recuperado con éxito"))
              .andExpect(jsonPath("$.datos.userId").value(userId))
              .andExpect(jsonPath("$.datos.fullName").value("John Doe"));
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUserById_UserNotFound_ReturnsError() throws Exception {
        // Arrange
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(null);

        // Act
        var result = mockMvc.perform(get("/api/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isNotFound())
              .andExpect(jsonPath("$.exito").value(false))
              .andExpect(jsonPath("$.mensaje").value("Error al recuperar el usuario: Usuario no encontrado"))
              .andExpect(jsonPath("$.datos").isEmpty());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void updateUser_UserExists_ReturnsUpdatedUser() throws Exception {
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
        var result = mockMvc.perform(put("/api/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fullName\":\"John Updated\",\"email\":\"john@example.com\"}"));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.exito").value(true))
              .andExpect(jsonPath("$.mensaje").value("Usuario actualizado con éxito"))
              .andExpect(jsonPath("$.datos.userId").value(userId))
              .andExpect(jsonPath("$.datos.fullName").value("John Updated"))
              .andExpect(jsonPath("$.datos.email").value("john@example.com"));
        verify(userService, times(1)).updateUser(any(User.class));
        verify(userService, times(2)).getUserById(userId);
    }

    @Test
    void updateUser_UserNotFound_ReturnsError() throws Exception {
        // Arrange
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(null);

        // Act
        var result = mockMvc.perform(put("/api/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fullName\":\"John Updated\"}"));

        // Assert
        result.andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.exito").value(false))
              .andExpect(jsonPath("$.mensaje").value("Error al actualizar el usuario: Usuario no encontrado"))
              .andExpect(jsonPath("$.datos").isEmpty());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void deleteUser_UserExists_ReturnsSuccess() throws Exception {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setFullName("John Doe");

        when(userService.getUserById(userId)).thenReturn(user);
        doNothing().when(userService).deleteUser(user);

        // Act
        var result = mockMvc.perform(delete("/api/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.exito").value(true))
              .andExpect(jsonPath("$.mensaje").value("Usuario eliminado con éxito"))
              .andExpect(jsonPath("$.datos").isEmpty());
        verify(userService, times(1)).getUserById(userId);
        verify(userService, times(1)).deleteUser(user);
    }

    @Test
    void deleteUser_UserNotFound_ReturnsError() throws Exception {
        // Arrange
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(null);

        // Act
        var result = mockMvc.perform(delete("/api/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.exito").value(false))
              .andExpect(jsonPath("$.mensaje").value("Error al eliminar el usuario: Usuario no encontrado"))
              .andExpect(jsonPath("$.datos").isEmpty());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void addRoleToUser_ReturnsNotImplemented() throws Exception {
        // Arrange
        Long userId = 1L;
        Long roleId = 1L;

        // Act
        var result = mockMvc.perform(post("/api/v1/users/{userId}/roles/{roleId}", userId, roleId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.exito").value(false))
              .andExpect(jsonPath("$.mensaje").value("Error al añadir rol al usuario: Funcionalidad no implementada"))
              .andExpect(jsonPath("$.datos").isEmpty());
    }

    @Test
    void removeRoleFromUser_ReturnsNotImplemented() throws Exception {
        // Arrange
        Long userId = 1L;
        Long roleId = 1L;

        // Act
        var result = mockMvc.perform(delete("/api/v1/users/{userId}/roles/{roleId}", userId, roleId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.exito").value(false))
              .andExpect(jsonPath("$.mensaje").value("Error al eliminar rol del usuario: Funcionalidad no implementada"))
              .andExpect(jsonPath("$.datos").isEmpty());
    }
}
