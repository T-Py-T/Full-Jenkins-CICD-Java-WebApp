package com.example.twitterapp.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PostEntityTest {

    @Test
    void gettersAndSettersRoundTrip() {
        Post post = new Post();
        User author = new User("kate", "hash");
        LocalDateTime created = LocalDateTime.of(2026, 1, 15, 10, 30);

        post.setId(7L);
        post.setContent("hello world");
        post.setCreatedAt(created);
        post.setUser(author);

        assertEquals(7L, post.getId());
        assertEquals("hello world", post.getContent());
        assertEquals(created, post.getCreatedAt());
        assertEquals("kate", post.getUser().getUsername());
    }

    @Test
    void newPostHasNullFields() {
        Post post = new Post();

        assertNull(post.getId());
        assertNull(post.getContent());
        assertNull(post.getCreatedAt());
        assertNull(post.getUser());
    }
}
