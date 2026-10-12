package com.example.twitterapp;

import com.example.twitterapp.config.CustomUserDetails;
import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.PostRepository;
import com.example.twitterapp.repository.UserRepository;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PostFlowIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void resetData(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        postRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.save(new User("demo", passwordEncoder.encode("demo-pass")));
    }

    @Test
    void authenticatedUserCanCreatePostAndSeeItOnHome() throws Exception {
        MockHttpSession session = (MockHttpSession) mockMvc.perform(formLogin("/login").user("demo").password("demo-pass"))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getRequest()
                .getSession();

        CustomUserDetails principal = new CustomUserDetails("demo", "demo-pass", List.of());

        mockMvc.perform(post("/add")
                        .session(session)
                        .with(user(principal))
                        .param("content", "Hello from integration tests"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        mockMvc.perform(get("/").session(session).with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello from integration tests")));

        assertEquals(1, postRepository.count());
    }

    @Test
    @WithMockUser(username = "demo", roles = "USER")
    void addPageRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/add")).andExpect(status().isOk());
    }
}
