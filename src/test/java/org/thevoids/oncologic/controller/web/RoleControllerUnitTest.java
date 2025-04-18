package org.thevoids.oncologic.controller.web;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.thevoids.oncologic.dto.PermissionDTO;
import org.thevoids.oncologic.dto.RoleDTO;
import org.thevoids.oncologic.dto.RoleWithPermissionsDTO;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.RolePermission;
import org.thevoids.oncologic.mapper.PermissionMapper;
import org.thevoids.oncologic.mapper.RoleMapper;
import org.thevoids.oncologic.service.RolePermissionService;
import org.thevoids.oncologic.service.RoleService;

public class RoleControllerUnitTest {

    @Mock
    private RoleService roleService;

    @Mock
    private RolePermissionService rolePermissionService;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private PermissionMapper permissionMapper;

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
        RoleWithPermissionsDTO roleDTO1 = new RoleWithPermissionsDTO(1L, "Admin", List.of());
        RoleWithPermissionsDTO roleDTO2 = new RoleWithPermissionsDTO(2L, "User", List.of());
        List<RoleWithPermissionsDTO> roleDTOs = Arrays.asList(roleDTO1, roleDTO2);

        when(roleService.getAllRoles()).thenReturn(roles);
        when(roleMapper.toRoleWithPermissionsDTO(role1)).thenReturn(roleDTO1);
        when(roleMapper.toRoleWithPermissionsDTO(role2)).thenReturn(roleDTO2);

        // Act
        String viewName = roleController.listRoles(model);

        // Assert
        assertEquals("roles/list", viewName);
        verify(model).addAttribute("roles", roleDTOs);
        verify(roleService).getAllRoles();
        verify(roleMapper, times(2)).toRoleWithPermissionsDTO(any(Role.class));
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

    @Test
    void managePermissions_ReturnsManagePermissionsView() {
        // Arrange
        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("Admin");

        Permission permission1 = new Permission();
        permission1.setPermissionId(1L);
        permission1.setPermissionName("READ");

        Permission permission2 = new Permission();
        permission2.setPermissionId(2L);
        permission2.setPermissionName("WRITE");

        RolePermission rolePermission = new RolePermission();
        rolePermission.setPermission(permission1);
        role.setRolePermissions(List.of(rolePermission));

        RoleWithPermissionsDTO roleWithPermissionsDTO = new RoleWithPermissionsDTO();
        roleWithPermissionsDTO.setRoleId(1L);
        roleWithPermissionsDTO.setRoleName("Admin");
        roleWithPermissionsDTO.setPermissions(List.of(new PermissionDTO(1L, "READ")));

        PermissionDTO permissionDTO2 = new PermissionDTO(2L, "WRITE");

        when(roleService.getRole(1L)).thenReturn(role);
        when(roleMapper.toRoleWithPermissionsDTO(role)).thenReturn(roleWithPermissionsDTO);
        when(rolePermissionService.getAllPermissions()).thenReturn(List.of(permission1, permission2));
        when(permissionMapper.toPermissionDTO(permission2)).thenReturn(permissionDTO2);

        // Act
        String viewName = roleController.managePermissions(1L, model);

        // Assert
        assertEquals("roles/manage_permissions", viewName);
        verify(model).addAttribute("role", roleWithPermissionsDTO);
        verify(model).addAttribute("permissions", List.of(permissionDTO2));
    }

    @Test
    void assignPermission_Success() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        // Act
        String viewName = roleController.assignPermission(roleId, permissionId, model);

        // Assert
        assertEquals("redirect:/web/roles/" + roleId + "/permissions", viewName);
        verify(rolePermissionService, times(1)).assignPermissionToRole(permissionId, roleId);
    }

    @Test
    void assignPermission_Failure() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;
        doThrow(new RuntimeException("Error assigning permission")).when(rolePermissionService).assignPermissionToRole(permissionId, roleId);

        // Act
        String viewName = roleController.assignPermission(roleId, permissionId, model);

        // Assert
        assertEquals("redirect:/web/roles/" + roleId + "/permissions", viewName);
        verify(model).addAttribute("error", "Error assigning permission");
    }

    @Test
    void removePermission_Success() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;

        // Act
        String viewName = roleController.removePermission(roleId, permissionId, model);

        // Assert
        assertEquals("redirect:/web/roles/" + roleId + "/permissions", viewName);
        verify(rolePermissionService, times(1)).removePermissionFromRole(permissionId, roleId);
    }

    @Test
    void removePermission_Failure() {
        // Arrange
        Long roleId = 1L;
        Long permissionId = 1L;
        doThrow(new RuntimeException("Error removing permission")).when(rolePermissionService).removePermissionFromRole(permissionId, roleId);

        // Act
        String viewName = roleController.removePermission(roleId, permissionId, model);

        // Assert
        assertEquals("redirect:/web/roles/" + roleId + "/permissions", viewName);
        verify(model).addAttribute("error", "Error removing permission");
    }
}