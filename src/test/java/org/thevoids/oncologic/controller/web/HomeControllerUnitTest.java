package org.thevoids.oncologic.controller.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.mockito.Mockito.when;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Collection;
import java.util.List;

public class HomeControllerUnitTest {

    private MockMvc mockMvc;

    @InjectMocks
    private HomeController homeController;

    @Mock
    private Authentication authentication;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configure a ViewResolver to resolve the "home" and "my_profile" views
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(homeController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void home_ReturnsHomeView() throws Exception {
        // Act
        var result = mockMvc.perform(get("/web/home"));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(view().name("home"));
    }

    @Test
    void showProfile_ReturnsProfileView() throws Exception {
        // Arrange
        when(authentication.getName()).thenReturn("");
        when(authentication.getAuthorities()).thenReturn(List.of());

        // Act
        var result = mockMvc.perform(get("/web/profile")
                .principal(authentication));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(view().name("my_profile"))
              .andExpect(model().attribute("username", ""))
              .andExpect(model().attributeExists("roles"))
              .andExpect(model().attributeExists("permissions"));
    }
}
