package org.thevoids.oncologic.controller.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thevoids.oncologic.dto.UserWithRolesDTO;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.mapper.RoleMapper;
import org.thevoids.oncologic.mapper.UserMapper;
import org.thevoids.oncologic.service.AssignedRoles;
import org.thevoids.oncologic.service.RoleService;
import org.thevoids.oncologic.service.UserService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private AssignedRoles assignedRolesService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

        @Test
    void listUsers_ReturnsUserListView() throws Exception {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        user.setFullName("John Doe");
        List<User> users = List.of(user);
    
        UserWithRolesDTO userDTO = new UserWithRolesDTO();
        userDTO.setUserId(1L);
        userDTO.setFullName("John Doe");
    
        when(userService.getAllUsers()).thenReturn(users);
        when(userMapper.toUserWithRolesDTO(user)).thenReturn(userDTO);
    
        // Act
        var result = mockMvc.perform(get("/web/users"));
    
        // Assert
        result.andExpect(status().isOk())
              .andExpect(view().name("users/list"))
              .andExpect(model().attributeExists("users"));
        verify(userService, times(1)).getAllUsers();
    }
    
    @Test
    void showRegisterForm_ReturnsRegisterView() throws Exception {
        // Arrange
        when(roleService.getAllRoles()).thenReturn(List.of());
    
        // Act
        var result = mockMvc.perform(get("/web/users/register"));
    
        // Assert
        result.andExpect(status().isOk())
              .andExpect(view().name("users/register"))
              .andExpect(model().attributeExists("user"))
              .andExpect(model().attributeExists("roles"));
        verify(roleService, times(1)).getAllRoles();
    }
    
    @Test
    void registerUser_SuccessfulRegistration_RedirectsToUserList() throws Exception {
        // Arrange
        User user = new User();
        user.setIdentification("123456");
    
        when(userService.createUser(any(User.class))).thenReturn(new User());
    
        // Act
        var result = mockMvc.perform(post("/web/users/register")
                            .flashAttr("user", user));
    
        // Assert
        result.andExpect(status().is3xxRedirection())
              .andExpect(redirectedUrl("/web/users"));
        verify(userService, times(1)).createUser(any(User.class));
    }
    
    @Test
    void registerUser_FailedRegistration_ReturnsRegisterViewWithError() throws Exception {
        // Arrange
        User user = new User();
        user.setIdentification("123456");
    
        doThrow(new IllegalArgumentException("Error creating user")).when(userService).createUser(any(User.class));
        when(roleService.getAllRoles()).thenReturn(List.of());
    
        // Act
        var result = mockMvc.perform(post("/web/users/register")
                            .flashAttr("user", user));
    
        // Assert
        result.andExpect(status().isOk())
              .andExpect(view().name("users/register"))
              .andExpect(model().attributeExists("error"))
              .andExpect(model().attributeExists("roles"));
        verify(userService, times(1)).createUser(any(User.class));
    }
    
    @Test
    void manageRoles_ReturnsManageRolesView() throws Exception {
        // Arrange
        User user = new User();
        user.setUserId(1L);
    
        UserWithRolesDTO userDTO = new UserWithRolesDTO();
        userDTO.setUserId(1L);
    
        when(userService.getUserById(1L)).thenReturn(user);
        when(userMapper.toUserWithRolesDTO(user)).thenReturn(userDTO);
        when(roleService.getAllRoles()).thenReturn(List.of());
    
        // Act
        var result = mockMvc.perform(get("/web/users/1/roles"));
    
        // Assert
        result.andExpect(status().isOk())
              .andExpect(view().name("users/manage_roles"))
              .andExpect(model().attributeExists("user"))
              .andExpect(model().attributeExists("roles"));
        verify(userService, times(1)).getUserById(1L);
    }
    
    @Test
    void assignRole_SuccessfulAssignment_RedirectsToManageRoles() throws Exception {
        // Arrange
        doNothing().when(assignedRolesService).assignRoleToUser(1L, 1L);
    
        // Act
        var result = mockMvc.perform(post("/web/users/1/roles/assign")
                            .param("roleId", "1"));
    
        // Assert
        result.andExpect(status().is3xxRedirection())
              .andExpect(redirectedUrl("/web/users/1/roles"));
        verify(assignedRolesService, times(1)).assignRoleToUser(1L, 1L);
    }
    
    @Test
    void removeRole_SuccessfulRemoval_RedirectsToManageRoles() throws Exception {
        // Arrange
        doNothing().when(assignedRolesService).removeRoleFromUser(1L, 1L);
    
        // Act
        var result = mockMvc.perform(post("/web/users/1/roles/remove")
                            .param("roleId", "1"));
    
        // Assert
        result.andExpect(status().is3xxRedirection())
              .andExpect(redirectedUrl("/web/users/1/roles"));
        verify(assignedRolesService, times(1)).removeRoleFromUser(1L, 1L);
    }
}