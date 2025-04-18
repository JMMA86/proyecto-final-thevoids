package org.thevoids.oncologic.controller.web;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.service.AssignedRoles;
import org.thevoids.oncologic.service.RoleService;
import org.thevoids.oncologic.service.UserService;

public class AuthControllerUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private AssignedRoles assignedRolesService;

    @Mock
    private Model model;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignup() {
        Role role1 = new Role();
        role1.setRoleId(1L);
        role1.setRoleName("Admin");

        Role role2 = new Role();
        role2.setRoleId(2L);
        role2.setRoleName("User");

        // Arrange
        when(roleService.getAllRoles()).thenReturn(Arrays.asList(role1, role2));

        // Act
        String viewName = authController.signup(model);

        // Assert
        assertEquals("auth/signup", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
        verify(model).addAttribute("roles", Arrays.asList(role1, role2));
        verify(roleService).getAllRoles();
    }

    @Test
    void testSignupCreate() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        user.setFullName("John Doe");
        Long roleId = 1L;

        when(userService.createUser(user)).thenReturn(user);

        // Act
        String viewName = authController.signupCreate(user, roleId);

        // Assert
        assertEquals("redirect:/web/auth/login", viewName);
        verify(userService).createUser(user);
        verify(assignedRolesService).assignRoleToUser(roleId, user.getUserId());
    }

    @Test
    void testLogin() {
        // Act
        String viewName = authController.login();

        // Assert
        assertEquals("auth/login", viewName);
    }
}