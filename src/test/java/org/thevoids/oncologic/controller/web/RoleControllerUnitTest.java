package org.thevoids.oncologic.controller.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.thevoids.oncologic.dto.RoleDTO;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.service.RoleService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoleControllerUnitTest {

    @Mock
    private RoleService roleService;

    @Mock
    private Model model;

    @InjectMocks
    private RoleController roleController;

    private Role role1;
    private Role role2;
    private List<Role> roles;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize roles
        role1 = new Role();
        role1.setRoleId(1L);
        role1.setRoleName("Admin");
        role2 = new Role();
        role2.setRoleId(2L);
        role2.setRoleName("User");
        roles = Arrays.asList(role1, role2);
    }

    @Test
    void testListRoles() {
        // Arrange
        when(roleService.getAllRoles()).thenReturn(roles);

        // Act
        String viewName = roleController.listRoles(model);

        // Assert
        assertEquals("roles/list", viewName);
        verify(model).addAttribute("roles", roles);
        verify(roleService).getAllRoles();
    }

    @Test
    void testShowCreateForm() {
        // Act
        String viewName = roleController.showCreateForm(model);

        // Assert
        assertEquals("roles/create", viewName);
        verify(model).addAttribute(eq("role"), any(RoleDTO.class));
    }

    @Test
    void testCreateRole_Success() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(null, "Admin");

        // Act
        String viewName = roleController.createRole(roleDTO, model);

        // Assert
        assertEquals("redirect:/web/roles", viewName);
        verify(roleService).createRole(any(Role.class));
    }

    @Test
    void testCreateRole_Failure() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(null, "Admin");
        doThrow(new RuntimeException("Error creating role")).when(roleService).createRole(any(Role.class));

        // Act
        String viewName = roleController.createRole(roleDTO, model);

        // Assert
        assertEquals("roles/create", viewName);
        verify(model).addAttribute("error", "Error creating role");
    }

    @Test
    void testShowEditForm_Success() {
        // Arrange
        when(roleService.getRole(1L)).thenReturn(role1);

        // Act
        String viewName = roleController.showEditForm(1L, model);

        // Assert
        assertEquals("roles/edit", viewName);
    }

    @Test
    void testShowEditForm_Failure() {
        // Arrange
        when(roleService.getRole(1L)).thenThrow(new RuntimeException("Role not found"));

        // Act
        String viewName = roleController.showEditForm(1L, model);

        // Assert
        assertEquals("redirect:/web/roles", viewName);
        verify(model).addAttribute("error", "Role not found");
    }

    @Test
    void testUpdateRole_Success() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(1L, "Admin");
        when(roleService.getRole(1L)).thenReturn(role1);

        // Act
        String viewName = roleController.updateRole(1L, roleDTO, model);

        // Assert
        assertEquals("redirect:/web/roles", viewName);
        verify(roleService).updateRole(role1);
    }

    @Test
    void testUpdateRole_Failure() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(1L, "Admin");
        when(roleService.getRole(1L)).thenThrow(new RuntimeException("Role not found"));

        // Act
        String viewName = roleController.updateRole(1L, roleDTO, model);

        // Assert
        assertEquals("roles/edit", viewName);
        verify(model).addAttribute("error", "Role not found");
    }

    @Test
    void testDeleteRole_Success() {
        // Arrange
        when(roleService.getRole(1L)).thenReturn(role1);

        // Act
        String viewName = roleController.deleteRole(1L);

        // Assert
        assertEquals("redirect:/web/roles", viewName);
        verify(roleService).deleteRole(role1);
    }

    @Test
    void testDeleteRole_Failure() {
        // Arrange
        when(roleService.getRole(1L)).thenThrow(new RuntimeException("Role not found"));

        // Act
        String viewName = roleController.deleteRole(1L);

        // Assert
        assertEquals("redirect:/web/roles?error=Role not found", viewName);
    }
}