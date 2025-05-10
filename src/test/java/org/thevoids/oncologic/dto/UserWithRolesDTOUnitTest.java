package org.thevoids.oncologic.dto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.thevoids.oncologic.dto.entity.RoleDTO;
import org.thevoids.oncologic.dto.entity.UserWithRolesDTO;

public class UserWithRolesDTOUnitTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        RoleDTO role1 = new RoleDTO(1L, "Admin");
        RoleDTO role2 = new RoleDTO(2L, "User");
        List<RoleDTO> roles = List.of(role1, role2);

        // Act
        UserWithRolesDTO userWithRolesDTO = new UserWithRolesDTO(1L, "John Doe", "john.doe@example.com", roles);

        // Assert
        assertEquals(1L, userWithRolesDTO.getUserId());
        assertEquals("John Doe", userWithRolesDTO.getFullName());
        assertEquals("john.doe@example.com", userWithRolesDTO.getEmail());
        assertEquals(roles, userWithRolesDTO.getRoles());
    }

    @Test
    void testSetters() {
        // Arrange
        UserWithRolesDTO userWithRolesDTO = new UserWithRolesDTO();
        RoleDTO role1 = new RoleDTO(1L, "Admin");
        RoleDTO role2 = new RoleDTO(2L, "User");
        List<RoleDTO> roles = List.of(role1, role2);

        // Act
        userWithRolesDTO.setUserId(1L);
        userWithRolesDTO.setFullName("John Doe");
        userWithRolesDTO.setEmail("john.doe@example.com");
        userWithRolesDTO.setRoles(roles);

        // Assert
        assertEquals(1L, userWithRolesDTO.getUserId());
        assertEquals("John Doe", userWithRolesDTO.getFullName());
        assertEquals("john.doe@example.com", userWithRolesDTO.getEmail());
        assertEquals(roles, userWithRolesDTO.getRoles());
    }

    @Test
    void testHasRole() {
        // Arrange
        RoleDTO role1 = new RoleDTO(1L, "Admin");
        RoleDTO role2 = new RoleDTO(2L, "User");
        List<RoleDTO> roles = List.of(role1, role2);
        UserWithRolesDTO userWithRolesDTO = new UserWithRolesDTO(1L, "John Doe", "john.doe@example.com", roles);

        // Act & Assert
        assertTrue(userWithRolesDTO.hasRole(1L)); // User has "Admin" role
        assertTrue(userWithRolesDTO.hasRole(2L)); // User has "User" role
        assertFalse(userWithRolesDTO.hasRole(3L)); // User does not have "Manager" role
    }
}
