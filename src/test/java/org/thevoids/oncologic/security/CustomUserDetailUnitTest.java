package org.thevoids.oncologic.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.thevoids.oncologic.entity.AssignedRole;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.entity.RolePermission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CustomUserDetailUnitTest {

    @Test
    public void testGetPassword() {
        // Arrange
        User user = new User();
        user.setPassword("securePassword123");
        CustomUserDetail customUserDetail = new CustomUserDetail(user);

        // Act
        String password = customUserDetail.getPassword();

        // Assert
        assertEquals("securePassword123", password);
    }

    @Test
    public void testGetUsername() {
        // Arrange
        User user = new User();
        user.setIdentification("123456789");
        CustomUserDetail customUserDetail = new CustomUserDetail(user);

        // Act
        String username = customUserDetail.getUsername();

        // Assert
        assertEquals("123456789", username);
    }

    @Test
    public void testGetAuthorities() {
        // Arrange
        Role role = new Role();
        role.setRoleName("ADMIN");
        role.setRolePermissions(Collections.emptyList()); // Initialize rolePermissions to an empty list

        Permission permission = new Permission();
        permission.setPermissionName("VIEW_USERS");

        RolePermission rolePermission = new RolePermission();
        rolePermission.setPermission(permission);

        role.setRolePermissions(Collections.singletonList(rolePermission)); // Add a permission to the role

        AssignedRole assignedRole = new AssignedRole();
        assignedRole.setRole(role);

        User user = new User();
        user.setAssignedRoles(Collections.singletonList(assignedRole));

        CustomUserDetail customUserDetail = new CustomUserDetail(user);

        // Act
        List<? extends GrantedAuthority> authorities = (List<? extends GrantedAuthority>) customUserDetail.getAuthorities();

        // Assert
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))); // Role with "ROLE_" prefix
        assertTrue(authorities.contains(new SimpleGrantedAuthority("VIEW_USERS"))); // Permission without prefix
    }

    @Test
    public void testGetAuthoritiesWithMock() {
        // Arrange
        Role role = Mockito.mock(Role.class);
        Mockito.when(role.getRoleName()).thenReturn("USER");

        Permission permission = Mockito.mock(Permission.class);
        Mockito.when(permission.getPermissionName()).thenReturn("VIEW_USERS");

        RolePermission rolePermission = Mockito.mock(RolePermission.class);
        Mockito.when(rolePermission.getPermission()).thenReturn(permission);

        Mockito.when(role.getRolePermissions()).thenReturn(Collections.singletonList(rolePermission));

        AssignedRole assignedRole = Mockito.mock(AssignedRole.class);
        Mockito.when(assignedRole.getRole()).thenReturn(role);

        User user = Mockito.mock(User.class);
        Mockito.when(user.getAssignedRoles()).thenReturn(Collections.singletonList(assignedRole));

        CustomUserDetail customUserDetail = new CustomUserDetail(user);

        // Act
        List<? extends GrantedAuthority> authorities = (List<? extends GrantedAuthority>) customUserDetail.getAuthorities();

        // Assert
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))); // Role with "ROLE_" prefix
        assertTrue(authorities.contains(new SimpleGrantedAuthority("VIEW_USERS"))); // Permission without prefix
    }
}