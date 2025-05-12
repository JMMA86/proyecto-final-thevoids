package org.thevoids.oncologic.controller.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
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
    void testLogin() {
        // Act
        String viewName = authController.login();

        // Assert
        assertEquals("auth/login", viewName);
    }
}