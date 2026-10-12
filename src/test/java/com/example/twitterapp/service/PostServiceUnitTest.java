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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceUnitTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void findAll_returnsRepositoryResults() {
        Post post = new Post();
        post.setContent("hello");
        when(postRepository.findAll()).thenReturn(List.of(post));

        assertThat(postService.findAll()).containsExactly(post);
    }

    @Test
    void save_stampsCreatedAtBeforePersisting() {
        Post post = new Post();
        post.setContent("new post");
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        postService.save(post);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        Post saved = captor.getValue();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getCreatedAt()).isAfter(before);
        assertThat(saved.getContent()).isEqualTo("new post");
    }
}
