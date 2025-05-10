package org.thevoids.oncologic.controller.web;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thevoids.oncologic.dto.custom.AuthResponseDTO;
import org.thevoids.oncologic.dto.entity.AuthRequest;
import org.thevoids.oncologic.dto.entity.RoleDTO;
import org.thevoids.oncologic.dto.entity.UserDTO;
import org.thevoids.oncologic.dto.entity.UserWithRolesDTO;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.mapper.RoleMapper;
import org.thevoids.oncologic.mapper.UserMapper;
import org.thevoids.oncologic.service.AssignedRoles;
import org.thevoids.oncologic.service.RoleService;
import org.thevoids.oncologic.service.UserService;

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
              .andExpect(model().attributeExists("userDTO"))
              .andExpect(model().attributeExists("roles"));
        verify(roleService, times(1)).getAllRoles();
    }
    
    @Test
    void registerUser_SuccessfulRegistration_RedirectsToUserList() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setIdentification("123456");

        User user = new User();
        user.setUserId(1L);

        when(userMapper.toUser(any(UserDTO.class))).thenReturn(user);
        when(userService.createUser(any(User.class))).thenReturn(user);

        // Act
        var result = mockMvc.perform(post("/web/users/register")
                            .flashAttr("userDTO", userDTO)
                            .param("roleId", "1"));

        // Assert
        result.andExpect(status().is3xxRedirection())
              .andExpect(redirectedUrl("/web/users"));
        verify(userMapper, times(1)).toUser(any(UserDTO.class));
        verify(userService, times(1)).createUser(any(User.class));
        verify(assignedRolesService, times(1)).assignRoleToUser(1L, user.getUserId());
    }
    
    @Test
    void registerUser_FailedRegistration_ReturnsRegisterViewWithError() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setIdentification("123456");

        when(userMapper.toUser(any(UserDTO.class))).thenReturn(new User());
        doThrow(new IllegalArgumentException("Error creating user")).when(userService).createUser(any(User.class));
        when(roleService.getAllRoles()).thenReturn(List.of());

        // Act
        var result = mockMvc.perform(post("/web/users/register")
                            .flashAttr("userDTO", userDTO)
                            .param("roleId", "1"));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(view().name("users/register"))
              .andExpect(model().attributeExists("error"))
              .andExpect(model().attributeExists("roles"));
        verify(userMapper, times(1)).toUser(any(UserDTO.class));
        verify(userService, times(1)).createUser(any(User.class));
        verify(roleService, times(1)).getAllRoles();
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
    void assignRole_FailedAssignment_ReturnsManageRolesViewWithError() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Error assigning role")).when(assignedRolesService).assignRoleToUser(1L, 1L);
    
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

    @Test
    void removeRole_FailedRemoval_ReturnsManageRolesViewWithError() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Error removing role")).when(assignedRolesService).removeRoleFromUser(1L, 1L);
    
        // Act
        var result = mockMvc.perform(post("/web/users/1/roles/remove")
                            .param("roleId", "1"));
    
        // Assert
        result.andExpect(status().is3xxRedirection())
              .andExpect(redirectedUrl("/web/users/1/roles"));
        verify(assignedRolesService, times(1)).removeRoleFromUser(1L, 1L);
    }

    @Test
    void testUserWithRolesDTO_HasRoleFunction() {
        // Arrange
        UserWithRolesDTO userWithRolesDTO = new UserWithRolesDTO();
        userWithRolesDTO.setUserId(1L);
        userWithRolesDTO.setFullName("John Doe");
        userWithRolesDTO.setEmail("john.doe@example.com");

        RoleDTO role1 = new RoleDTO(1L, "Admin");
        RoleDTO role2 = new RoleDTO(2L, "User");
        userWithRolesDTO.setRoles(List.of(role1, role2));

        // Act & Assert
        assertTrue(userWithRolesDTO.hasRole(1L));
        assertTrue(userWithRolesDTO.hasRole(2L));
        assertFalse(userWithRolesDTO.hasRole(3L));
    }

    @Test
    void testAuthRequestAndAuthResponse() {
        // Arrange
        AuthRequest authRequest = new AuthRequest("testUser", "password");
        AuthResponseDTO authResponse = new AuthResponseDTO("sampleAccessToken", "testUser");

        // Act & Assert
        assertEquals("testUser", authRequest.getUsername());
        assertEquals("password", authRequest.getPassword());
        assertEquals("sampleAccessToken", authResponse.getToken());
        assertEquals("testUser", authResponse.getUsername());
    }

    @Test
    void manageRoles_FiltersOutAssignedRoles() throws Exception {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        user.setFullName("John Doe");

        RoleDTO role1 = new RoleDTO(1L, "Admin");
        RoleDTO role2 = new RoleDTO(2L, "User");
        RoleDTO role3 = new RoleDTO(3L, "Manager");

        UserWithRolesDTO userWithRolesDTO = new UserWithRolesDTO();
        userWithRolesDTO.setUserId(1L);
        userWithRolesDTO.setFullName("John Doe");
        userWithRolesDTO.setRoles(List.of(role1, role2)); // User already has "Admin" and "User" roles

        when(userService.getUserById(1L)).thenReturn(user);
        when(userMapper.toUserWithRolesDTO(user)).thenReturn(userWithRolesDTO);

        Role roleEntity1 = new Role();
        roleEntity1.setRoleId(1L);
        roleEntity1.setRoleName("Admin");

        Role roleEntity2 = new Role();
        roleEntity2.setRoleId(2L);
        roleEntity2.setRoleName("User");

        Role roleEntity3 = new Role();
        roleEntity3.setRoleId(3L);
        roleEntity3.setRoleName("Manager");

        when(roleService.getAllRoles()).thenReturn(List.of(roleEntity1, roleEntity2, roleEntity3));
        when(roleMapper.toRoleDTO(roleEntity1)).thenReturn(role1);
        when(roleMapper.toRoleDTO(roleEntity2)).thenReturn(role2);
        when(roleMapper.toRoleDTO(roleEntity3)).thenReturn(role3);

        // Act
        var result = mockMvc.perform(get("/web/users/1/roles"));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(view().name("users/manage_roles"))
              .andExpect(model().attributeExists("user"))
              .andExpect(model().attributeExists("roles"));

        verify(userService, times(1)).getUserById(1L);
        verify(roleService, times(1)).getAllRoles();

        // Validate that only unassigned roles are included
        @SuppressWarnings({ "unchecked", "null" })
        List<RoleDTO> filteredRoles = (List<RoleDTO>) result.andReturn().getModelAndView().getModel().get("roles");
        assertEquals(1, filteredRoles.size());
        assertEquals(3L, filteredRoles.get(0).getRoleId()); // Only "Manager" role should remain
    }
}