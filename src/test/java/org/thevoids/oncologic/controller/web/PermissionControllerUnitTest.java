package org.thevoids.oncologic.controller.web;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.thevoids.oncologic.dto.PermissionDTO;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.mapper.PermissionMapper;
import org.thevoids.oncologic.service.PermissionService;

public class PermissionControllerUnitTest {

    @Mock
    private PermissionService permissionService;

    @Mock
    private PermissionMapper permissionMapper;

    @Mock
    private Model model;

    @InjectMocks
    private PermissionController permissionController;

    private Permission permission1;
    private Permission permission2;
    private List<Permission> permissions;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
    void testShowEditForm_Success() {
        // Arrange
        when(permissionService.getPermission(1L)).thenReturn(Optional.of(permission1));

        // Act
        String viewName = permissionController.showEditForm(1L, model);

        // Assert
        assertEquals("permissions/edit", viewName);
        ArgumentCaptor<PermissionDTO> captor = ArgumentCaptor.forClass(PermissionDTO.class);
        verify(model).addAttribute(eq("permission"), captor.capture());
        PermissionDTO capturedPermission = captor.getValue();
        assertEquals(permission1.getPermissionId(), capturedPermission.getPermissionId());
        assertEquals(permission1.getPermissionName(), capturedPermission.getPermissionName());
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
    void testUpdatePermission_Success() {
        // Arrange
        PermissionDTO updatedPermissionDTO = new PermissionDTO(1L, "UPDATED_READ");

        // Act
        String viewName = permissionController.updatePermission(1L, updatedPermissionDTO, model);

        // Assert
        assertEquals("redirect:/web/permissions", viewName);
        ArgumentCaptor<Permission> captor = ArgumentCaptor.forClass(Permission.class);
        verify(permissionService).updatePermission(captor.capture());
        Permission capturedPermission = captor.getValue();
        assertEquals(updatedPermissionDTO.getPermissionId(), capturedPermission.getPermissionId());
        assertEquals(updatedPermissionDTO.getPermissionName(), capturedPermission.getPermissionName());
    }

    @Test
    void testUpdatePermission_Failure() {
        // Arrange
        PermissionDTO updatedPermissionDTO = new PermissionDTO(1L, "UPDATED_READ");
        doThrow(new RuntimeException("Error updating permission")).when(permissionService).updatePermission(any(Permission.class));

        // Act
        String viewName = permissionController.updatePermission(1L, updatedPermissionDTO, model);

        // Assert
        assertEquals("permissions/edit", viewName);
        verify(model).addAttribute("error", "Error updating permission");
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