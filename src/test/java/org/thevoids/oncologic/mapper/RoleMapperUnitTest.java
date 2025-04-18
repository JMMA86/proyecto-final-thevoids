package org.thevoids.oncologic.mapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.thevoids.oncologic.dto.PermissionDTO;
import org.thevoids.oncologic.dto.RoleDTO;
import org.thevoids.oncologic.dto.RoleWithPermissionsDTO;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.RolePermission;

public class RoleMapperUnitTest {

    private final RoleMapper roleMapper = RoleMapper.INSTANCE;

    @Test
    public void testToRoleDTO() {
        // Arrange
        Role role = new Role();
        role.setRoleId(1L); 
        role.setRoleName("ADMIN");

        // Act
        RoleDTO roleDTO = roleMapper.toRoleDTO(role);

        // Assert
        assertEquals(role.getRoleId(), roleDTO.getRoleId()); 
        assertEquals(role.getRoleName(), roleDTO.getRoleName());
    }

    @Test
    public void testToRoleDTOWithMock() {
        // Arrange
        Role role = mock(Role.class);
        when(role.getRoleId()).thenReturn(2L); 
        when(role.getRoleName()).thenReturn("USER");

        // Act
        RoleDTO roleDTO = roleMapper.toRoleDTO(role);

        // Assert
        assertEquals(2L, roleDTO.getRoleId()); 
        assertEquals("USER", roleDTO.getRoleName());
    }

    @Test
    void testToRoleDTO_NullInput() {
        // Act
        RoleDTO roleDTO = roleMapper.toRoleDTO(null);

        // Assert
        assertNull(roleDTO);
    }

    @Test
    void testToRoleWithPermissionsDTO_NullInput() {
        // Act
        RoleWithPermissionsDTO roleWithPermissionsDTO = roleMapper.toRoleWithPermissionsDTO(null);

        // Assert
        assertNull(roleWithPermissionsDTO);
    }

    @Test
    void testToRoleWithPermissionsDTO_ValidInput() {
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

        RolePermission rolePermission1 = new RolePermission();
        rolePermission1.setPermission(permission1);

        RolePermission rolePermission2 = new RolePermission();
        rolePermission2.setPermission(permission2);

        role.setRolePermissions(List.of(rolePermission1, rolePermission2));

        // Act
        RoleWithPermissionsDTO roleWithPermissionsDTO = roleMapper.toRoleWithPermissionsDTO(role);

        // Assert
        assertNotNull(roleWithPermissionsDTO);
        assertEquals(role.getRoleId(), roleWithPermissionsDTO.getRoleId());
        assertEquals(role.getRoleName(), roleWithPermissionsDTO.getRoleName());
        assertNotNull(roleWithPermissionsDTO.getPermissions());
        assertEquals(2, roleWithPermissionsDTO.getPermissions().size());
        assertEquals(permission1.getPermissionId(), roleWithPermissionsDTO.getPermissions().get(0).getPermissionId());
        assertEquals(permission1.getPermissionName(), roleWithPermissionsDTO.getPermissions().get(0).getPermissionName());
        assertEquals(permission2.getPermissionId(), roleWithPermissionsDTO.getPermissions().get(1).getPermissionId());
        assertEquals(permission2.getPermissionName(), roleWithPermissionsDTO.getPermissions().get(1).getPermissionName());
    }

    @Test
    void testMapRolePermissionsToPermissionDTOs() {
        // Arrange
        Permission permission1 = new Permission();
        permission1.setPermissionId(1L);
        permission1.setPermissionName("READ");

        Permission permission2 = new Permission();
        permission2.setPermissionId(2L);
        permission2.setPermissionName("WRITE");

        RolePermission rolePermission1 = new RolePermission();
        rolePermission1.setPermission(permission1);

        RolePermission rolePermission2 = new RolePermission();
        rolePermission2.setPermission(permission2);

        List<RolePermission> rolePermissions = List.of(rolePermission1, rolePermission2);

        // Act
        List<PermissionDTO> permissionDTOs = RoleMapper.mapRolePermissionsToPermissionDTOs(rolePermissions);

        // Assert
        assertNotNull(permissionDTOs);
        assertEquals(2, permissionDTOs.size());
        assertEquals(permission1.getPermissionId(), permissionDTOs.get(0).getPermissionId());
        assertEquals(permission1.getPermissionName(), permissionDTOs.get(0).getPermissionName());
        assertEquals(permission2.getPermissionId(), permissionDTOs.get(1).getPermissionId());
        assertEquals(permission2.getPermissionName(), permissionDTOs.get(1).getPermissionName());
    }

    @Test
    void testLambdaFunctionForMappingRolePermission() {
        // Arrange
        Permission permission = new Permission();
        permission.setPermissionId(1L);
        permission.setPermissionName("EXECUTE");

        RolePermission rolePermission = new RolePermission();
        rolePermission.setPermission(permission);

        // Act
        PermissionDTO permissionDTO = RoleMapper.mapRolePermissionsToPermissionDTOs(List.of(rolePermission)).get(0);

        // Assert
        assertNotNull(permissionDTO);
        assertEquals(permission.getPermissionId(), permissionDTO.getPermissionId());
        assertEquals(permission.getPermissionName(), permissionDTO.getPermissionName());
    }
}