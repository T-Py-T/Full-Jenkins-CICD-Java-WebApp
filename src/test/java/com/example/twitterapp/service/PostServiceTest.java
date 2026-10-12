package com.example.twitterapp.service;

import com.example.twitterapp.model.Post;
import com.example.twitterapp.model.User;
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
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void saveSetsCreatedAtBeforePersisting() {
        Post post = new Post();
        post.setContent("hello");
        post.setUser(new User("demo", "hash"));

        postService.save(post);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        LocalDateTime createdAt = captor.getValue().getCreatedAt();
        assertNotNull(createdAt);
        assertEquals("hello", captor.getValue().getContent());
    }

    @Test
    void findAllDelegatesToRepository() {
        Post first = new Post();
        first.setContent("one");
        when(postRepository.findAll()).thenReturn(List.of(first));

        assertEquals(1, postService.findAll().size());
        assertEquals("one", postService.findAll().get(0).getContent());
    }
}
