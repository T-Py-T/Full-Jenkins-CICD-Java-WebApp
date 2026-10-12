package com.example.twitterapp.service;

import com.example.twitterapp.model.Post;
import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.PostRepository;
import com.example.twitterapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

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
    void saveSetsCreatedAtAndPersistsPost() {
        User author = userRepository.save(new User("helen", "hash"));
        Post post = new Post();
        post.setContent("service-layer tweet");
        post.setUser(author);

        postService.save(post);

        List<Post> stored = postRepository.findAll();
        assertEquals(1, stored.size());
        assertEquals("service-layer tweet", stored.get(0).getContent());
        assertNotNull(stored.get(0).getCreatedAt());
        assertEquals("helen", stored.get(0).getUser().getUsername());
    }

    @Test
    void findAllReturnsEveryPersistedPost() {
        User author = userRepository.save(new User("ivan", "hash"));
        Post first = new Post();
        first.setContent("first");
        first.setUser(author);
        postService.save(first);

        Post second = new Post();
        second.setContent("second");
        second.setUser(author);
        postService.save(second);

        List<Post> all = postService.findAll();

        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(p -> "first".equals(p.getContent())));
        assertTrue(all.stream().anyMatch(p -> "second".equals(p.getContent())));
    }
}
