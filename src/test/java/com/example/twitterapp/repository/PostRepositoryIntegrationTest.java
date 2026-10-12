package com.example.twitterapp.repository;

import com.example.twitterapp.model.Post;
import com.example.twitterapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PostRepositoryIntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void persistsPostLinkedToUser() {
        User author = userRepository.save(new User("author", "hashed"));

        Post post = new Post();
        post.setContent("Hello from the test suite");
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(author);

        Post saved = postRepository.save(post);

        assertNotNull(saved.getId());
        assertEquals(1, postRepository.findAll().size());
        assertEquals("author", postRepository.findAll().get(0).getUser().getUsername());
    }
}
