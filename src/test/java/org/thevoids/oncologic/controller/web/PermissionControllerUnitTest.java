package org.thevoids.oncologic.controller.web;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.thevoids.oncologic.dto.entity.PermissionDTO;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.mapper.PermissionMapper;
import org.thevoids.oncologic.service.PermissionService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

public class PermissionControllerUnitTest {

    @Mock
    private PermissionService permissionService;

    @Mock
    private PermissionMapper permissionMapper;

    @Mock
    private Model model;

    @InjectMocks
    private PermissionController permissionController;

    private MockMvc mockMvc;

    private Permission permission1;
    private Permission permission2;
    private List<Permission> permissions;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(permissionController).build();

        // Initialize permissions with unique IDs
        permission1 = new Permission();
        permission1.setPermissionId(1L); // Unique ID
        permission1.setPermissionName("READ");

        permission2 = new Permission();
        permission2.setPermissionId(2L); // Unique ID
        permission2.setPermissionName("WRITE");

        permissions = Arrays.asList(permission1, permission2);
    }

    @Test
    void testListPermissions() {
        // Arrange
        when(permissionService.getAllPermissions()).thenReturn(permissions);

        // Act
        String viewName = permissionController.listPermissions(model);

        // Assert
        assertEquals("permissions/list", viewName);
        verify(model).addAttribute("permissions", permissions);
        verify(permissionService).getAllPermissions();
    }

    @Test
    void testShowCreateForm() {
        // Act
        String viewName = permissionController.showCreateForm(model);

        // Assert
        assertEquals("permissions/create", viewName);
        verify(model).addAttribute(eq("permissionDTO"), any(PermissionDTO.class));
    }

    @Test
    void testCreatePermission_Success() {
        // Arrange
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionName("READ");

        Permission permission = new Permission();
        permission.setPermissionName("READ");

        when(permissionMapper.toPermission(permissionDTO)).thenReturn(permission);

        // Act
        String viewName = permissionController.createPermission(permissionDTO, model);

        // Assert
        assertEquals("redirect:/web/permissions", viewName);
        verify(permissionService).createPermission(permission);
    }

    @Test
    void testCreatePermission_Failure() {
        // Arrange
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionName("READ");

        Permission permission = new Permission();
        permission.setPermissionName("READ");

        when(permissionMapper.toPermission(permissionDTO)).thenReturn(permission);
        doThrow(new RuntimeException("Error creating permission")).when(permissionService).createPermission(permission);

        // Act
        String viewName = permissionController.createPermission(permissionDTO, model);

        // Assert
        assertEquals("permissions/create", viewName);
        verify(model).addAttribute(eq("error"), eq("Error creating permission"));
    }

    @Test
    void testCreatePermission_WithIdProvided_Failure() {
        // Arrange
        PermissionDTO permissionDTO = new PermissionDTO(3L, "READ"); // Unique ID
        Permission permission = new Permission();
        permission.setPermissionId(3L); // Unique ID
        permission.setPermissionName("READ");

        when(permissionMapper.toPermission(permissionDTO)).thenReturn(permission);
        doThrow(new IllegalArgumentException("ID must not be provided when creating a new permission")).when(permissionService).createPermission(permission);

        // Act
        String viewName = permissionController.createPermission(permissionDTO, model);

        // Assert
        assertEquals("permissions/create", viewName);
        verify(model).addAttribute(eq("error"), eq("ID must not be provided when creating a new permission"));
    }

    @Test
    void testShowEditForm_Success() throws Exception {
        // Arrange
        Long permissionId = 1L;
        Permission permission = new Permission();
        permission.setPermissionId(permissionId);
        permission.setPermissionName("VIEW_USERS");

        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionId(permissionId);
        permissionDTO.setPermissionName("VIEW_USERS");

        when(permissionService.getPermission(permissionId)).thenReturn(Optional.of(permission));
        when(permissionMapper.toPermissionDTO(permission)).thenReturn(permissionDTO);

        // Act & Assert
        mockMvc.perform(get("/web/permissions/{id}/edit", permissionId))
                .andExpect(status().isOk())
                .andExpect(view().name("permissions/edit"))
                .andExpect(model().attributeExists("permissionDTO"))
                .andExpect(model().attribute("permissionDTO", permissionDTO));
    }

    @Test
    void testShowEditForm_Failure() {
        // Arrange
        when(permissionService.getPermission(1L)).thenReturn(Optional.empty());

        // Act
        String viewName = permissionController.showEditForm(1L, model);

        // Assert
        assertEquals("redirect:/web/permissions", viewName);
        verify(model).addAttribute("error", "Permission not found");
    }

    @Test
    void updatePermission_WhenPermissionExists_UpdatesSuccessfully() throws Exception {
        // Arrange
        Long permissionId = 1L;
        Permission permission = new Permission();
        permission.setPermissionId(permissionId);
        permission.setPermissionName("VIEW_USERS");

        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionId(permissionId);
        permissionDTO.setPermissionName("EDIT_USERS");

        when(permissionService.getPermission(permissionId)).thenReturn(Optional.of(permission));
        when(permissionMapper.toPermissionDTO(permission)).thenReturn(permissionDTO);

        // Act & Assert
        mockMvc.perform(post("/web/permissions/{id}/edit", permissionId)
                        .flashAttr("permissionDTO", permissionDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/permissions"));

        verify(permissionService, times(1)).updatePermission(permission);
        assertEquals("EDIT_USERS", permission.getPermissionName());
    }

    @Test
    void updatePermission_WhenPermissionDoesNotExist_ReturnsError() throws Exception {
        // Arrange
        Long permissionId = 1L;
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionId(permissionId);
        permissionDTO.setPermissionName("EDIT_USERS");

        when(permissionService.getPermission(permissionId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/web/permissions/{id}/edit", permissionId)
                        .flashAttr("permissionDTO", permissionDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/permissions?error=Permission not found"));

        verify(permissionService, never()).updatePermission(any(Permission.class));
    }

    @Test
    void testDeletePermission_Success() {
        // Act
        String viewName = permissionController.deletePermission(1L);

        // Assert
        assertEquals("redirect:/web/permissions", viewName);
        verify(permissionService).deletePermission(1L);
    }

    @Test
    void testDeletePermission_Failure() {
        // Arrange
        doThrow(new RuntimeException("Error deleting permission")).when(permissionService).deletePermission(1L);

        // Act
        String viewName = permissionController.deletePermission(1L);

        // Assert
        assertEquals("redirect:/web/permissions?error=Error deleting permission", viewName);
    }
}