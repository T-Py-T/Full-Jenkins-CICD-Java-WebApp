package com.example.twitterapp.controller;

import com.example.twitterapp.config.CustomUserDetails;
import com.example.twitterapp.model.Post;
import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.PostRepository;
import com.example.twitterapp.repository.UserRepository;
import com.example.twitterapp.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class PostControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

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
    void authenticatedUserCanCreatePostAndSeeItOnHome() throws Exception {
        User author = userRepository.save(new User("poster", passwordEncoder.encode("pw")));
        CustomUserDetails principal = new CustomUserDetails(
                author.getUsername(),
                author.getPassword(),
                List.of(new SimpleGrantedAuthority("USER")));

        mockMvc.perform(post("/add")
                        .with(user(principal))
                        .param("content", "Integration test tweet"))
                .andExpect(redirectedUrl("/"));

        List<Post> posts = postRepository.findAll();
        assertEquals(1, posts.size());
        assertEquals("Integration test tweet", posts.get(0).getContent());
        assertNotNull(posts.get(0).getCreatedAt());
        assertEquals("poster", posts.get(0).getUser().getUsername());

        mockMvc.perform(get("/").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Integration test tweet")));
    }

    @Test
    void addFormRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/add"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void addFormIsAvailableToAuthenticatedUser() throws Exception {
        User author = userRepository.save(new User("writer", passwordEncoder.encode("pw")));
        CustomUserDetails principal = new CustomUserDetails(
                author.getUsername(),
                author.getPassword(),
                List.of(new SimpleGrantedAuthority("USER")));

        mockMvc.perform(get("/add").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("add"));
    }

    @Test
    void postServiceSetsCreatedAtOnSave() {
        User author = userRepository.save(new User("timekeeper", "hash"));
        Post post = new Post();
        post.setContent("timestamped");
        post.setUser(author);

        postService.save(post);

        assertNotNull(postRepository.findAll().get(0).getCreatedAt());
    }
}
