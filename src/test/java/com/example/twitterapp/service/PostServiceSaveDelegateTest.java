package com.example.twitterapp.service;

import com.example.twitterapp.model.Post;
import com.example.twitterapp.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceSaveDelegateTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void saveSetsCreatedAtBeforePersisting() {
        Post post = new Post();
        post.setContent("timestamp check");

        postService.save(post);

        ArgumentCaptor<Post> saved = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(saved.capture());
        LocalDateTime createdAt = saved.getValue().getCreatedAt();
        assertNotNull(createdAt);
        assertTrue(!createdAt.isAfter(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void findAllReturnsRepositoryResults() {
        Post post = new Post();
        when(postRepository.findAll()).thenReturn(List.of(post));

        var result = postService.findAll();

        assertEquals(1, result.size());
        assertSame(post, result.get(0));
        verify(postRepository).findAll();
    }
}
