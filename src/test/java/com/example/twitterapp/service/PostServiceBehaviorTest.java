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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceBehaviorTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void saveSetsCreatedAtBeforePersisting() {
        Post post = new Post();
        post.setContent("hello world");

        postService.save(post);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        Post saved = captor.getValue();
        assertNotNull(saved.getCreatedAt());
        assertEquals("hello world", saved.getContent());
    }

    @Test
    void findAllReturnsRepositoryResults() {
        Post post = new Post();
        post.setContent("feed item");
        when(postRepository.findAll()).thenReturn(List.of(post));

        List<Post> posts = postService.findAll();

        assertEquals(1, posts.size());
        assertEquals("feed item", posts.get(0).getContent());
    }
}
