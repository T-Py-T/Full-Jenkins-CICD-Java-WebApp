package com.example.twitterapp.service;

import com.example.twitterapp.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PostServiceNullUserTest {

    @Autowired
    private PostService postService;

    @Test
    void saveRejectsPostWithoutUser() {
        Post post = new Post();
        post.setContent("orphan post");

        assertThrows(IllegalArgumentException.class, () -> postService.save(post));
    }
}
