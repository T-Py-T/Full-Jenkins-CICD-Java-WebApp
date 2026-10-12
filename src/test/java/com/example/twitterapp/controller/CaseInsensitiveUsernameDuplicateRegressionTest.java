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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class CaseInsensitiveUsernameDuplicateRegressionTest {

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
    void rejectsRegistrationWhenUsernameDiffersOnlyByCase() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "UniqueUser")
                        .param("password", "password1"))
                .andExpect(status().is3xxRedirection());

        long afterFirst = userRepository.count();

        mockMvc.perform(post("/register")
                        .param("username", "uniqueuser")
                        .param("password", "password2"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("Userexist"));

        assertEquals(afterFirst, userRepository.count());
    }
}
