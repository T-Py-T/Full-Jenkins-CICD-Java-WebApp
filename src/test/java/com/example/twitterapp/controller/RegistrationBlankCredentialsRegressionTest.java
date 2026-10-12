package com.example.twitterapp.controller;

import com.example.twitterapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class RegistrationBlankCredentialsRegressionTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void rejectsWhitespaceOnlyUsernameWithoutCreatingUser() throws Exception {
        long before = userRepository.count();

        mockMvc.perform(post("/register")
                        .param("username", "   ")
                        .param("password", "secret"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));

        assertEquals(before, userRepository.count());
    }

    @Test
    void rejectsEmptyPasswordWithoutCreatingUser() throws Exception {
        long before = userRepository.count();

        mockMvc.perform(post("/register")
                        .param("username", "valid-user")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));

        assertEquals(before, userRepository.count());
    }
}
