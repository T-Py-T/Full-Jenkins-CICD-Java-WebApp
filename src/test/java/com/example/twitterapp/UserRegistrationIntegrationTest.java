package com.example.twitterapp;

import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.PostRepository;
import com.example.twitterapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class UserRegistrationIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void registerCreatesUserAndAllowsLogin() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "alice")
                        .param("password", "secret-pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?success"));

        assertNotNull(userRepository.findByUsername("alice"));

        mockMvc.perform(formLogin("/login").user("alice").password("secret-pass"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void duplicateUsernameStaysOnRegisterPage() throws Exception {
        userRepository.save(new User("bob", passwordEncoder.encode("existing")));

        mockMvc.perform(post("/register")
                        .param("username", "bob")
                        .param("password", "new-pass"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void loginPageIsReachableWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk());
    }
}
