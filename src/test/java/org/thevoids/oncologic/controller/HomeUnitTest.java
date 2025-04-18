package org.thevoids.oncologic.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HomeUnitTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Home homeController = new Home();
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    void home_RedirectsToWebHome() throws Exception {
        // Act
        var result = mockMvc.perform(get("/"));

        // Assert
        result.andExpect(status().is3xxRedirection())
              .andExpect(redirectedUrl("/web/home"));
    }
}