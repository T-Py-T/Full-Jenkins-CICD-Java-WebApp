package com.example.twitterapp.controller;

import com.example.twitterapp.model.Post;
import com.example.twitterapp.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class HomeOrphanPostAuthorRegressionTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        postRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "reader")
    void homeRendersOrphanPostsWithoutAuthor() throws Exception {
        Post orphan = new Post();
        orphan.setContent("Post from deleted account");
        orphan.setUser(null);
        orphan.setCreatedAt(LocalDateTime.now());
        postRepository.save(orphan);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Posted by:")))
                .andExpect(content().string(containsString("Unknown")));
    }
}
