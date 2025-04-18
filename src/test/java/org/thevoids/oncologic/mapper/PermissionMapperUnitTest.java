package org.thevoids.oncologic.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.thevoids.oncologic.dto.PermissionDTO;
import org.thevoids.oncologic.entity.Permission;

public class PermissionMapperUnitTest {

    private final PermissionMapper permissionMapper = PermissionMapper.INSTANCE;

    @Test
    void testToPermissionDTO() {
        // Arrange
        Permission permission = new Permission();
        permission.setPermissionId(1L);
        permission.setPermissionName("READ");

        // Act
        PermissionDTO permissionDTO = permissionMapper.toPermissionDTO(permission);

        // Assert
        assertNotNull(permissionDTO);
        assertEquals(permission.getPermissionId(), permissionDTO.getPermissionId());
        assertEquals(permission.getPermissionName(), permissionDTO.getPermissionName());
    }

    @Test
    void testToPermission() {
        // Arrange
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionId(1L);
        permissionDTO.setPermissionName("WRITE");

        // Act
        Permission permission = permissionMapper.toPermission(permissionDTO);

        // Assert
        assertNotNull(permission);
        assertEquals(permissionDTO.getPermissionId(), permission.getPermissionId());
        assertEquals(permissionDTO.getPermissionName(), permission.getPermissionName());
    }

    @Test
    void testToPermissionDTO_NullInput() {
        // Act
        PermissionDTO permissionDTO = permissionMapper.toPermissionDTO(null);

        // Assert
        assertNull(permissionDTO);
    }

    @Test
    void testToPermission_NullInput() {
        // Act
        Permission permission = permissionMapper.toPermission(null);

        // Assert
        assertNull(permission);
    }
}
