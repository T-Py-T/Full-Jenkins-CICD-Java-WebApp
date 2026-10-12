package com.example.twitterapp.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PostEntityPropertyRoundTripTest {

    @Test
    void gettersReflectAssignedFields() {
        User author = new User("grace", "pw");
        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 12, 18, 0);

        Post post = new Post();
        post.setId(11L);
        post.setContent("Hello timeline");
        post.setCreatedAt(createdAt);
        post.setUser(author);

        assertThat(post.getId()).isEqualTo(11L);
        assertThat(post.getContent()).isEqualTo("Hello timeline");
        assertThat(post.getCreatedAt()).isEqualTo(createdAt);
        assertThat(post.getUser()).isSameAs(author);
    }
}
