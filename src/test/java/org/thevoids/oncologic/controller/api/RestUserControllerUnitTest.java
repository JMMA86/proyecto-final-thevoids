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
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thevoids.oncologic.dto.entity.UserDTO;
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
}