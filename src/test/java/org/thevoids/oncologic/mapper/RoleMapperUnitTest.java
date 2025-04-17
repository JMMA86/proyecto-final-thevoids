package org.thevoids.oncologic.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.thevoids.oncologic.dto.RoleDTO;
import org.thevoids.oncologic.entity.Role;

public class RoleMapperUnitTest {

    @Test
    public void testToRoleDTO() {
        // Arrange
        Role role = new Role();
        role.setRoleId(1L); 
        role.setRoleName("ADMIN");

        RoleMapper roleMapper = RoleMapper.INSTANCE;

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

        RoleMapper roleMapper = RoleMapper.INSTANCE;

        // Act
        RoleDTO roleDTO = roleMapper.toRoleDTO(role);

        // Assert
        assertEquals(2L, roleDTO.getRoleId()); 
        assertEquals("USER", roleDTO.getRoleName());
    }
}