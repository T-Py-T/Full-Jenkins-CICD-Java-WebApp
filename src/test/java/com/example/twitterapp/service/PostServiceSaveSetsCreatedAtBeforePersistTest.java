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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceSaveSetsCreatedAtBeforePersistTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void saveStampsCreatedAtBeforeDelegatingToRepository() {
        Post post = new Post();
        post.setContent("status");
        post.setUser(new User("heidi", "pw"));

        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        postService.save(post);
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertThat(post.getCreatedAt()).isAfter(before).isBefore(after);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        assertThat(captor.getValue().getCreatedAt()).isEqualTo(post.getCreatedAt());
    }
}
