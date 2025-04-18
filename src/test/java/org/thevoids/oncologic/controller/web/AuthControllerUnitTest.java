package org.thevoids.oncologic.controller.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.service.RoleService;
import org.thevoids.oncologic.service.UserService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

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
        user.setIdentification("123");
        user.setPassword("password");

        // Act
        String viewName = authController.signupCreate(user);

        // Assert
        assertEquals("redirect:/web/auth/login", viewName);
        verify(userService).createUser(user);
    }

    @Test
    void testLogin() {
        // Act
        String viewName = authController.login();

        // Assert
        assertEquals("auth/login", viewName);
    }
}