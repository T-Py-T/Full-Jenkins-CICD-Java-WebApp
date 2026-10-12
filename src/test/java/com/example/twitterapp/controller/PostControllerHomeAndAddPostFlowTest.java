package com.example.twitterapp.controller;

import com.example.twitterapp.config.CustomUserDetails;
import com.example.twitterapp.model.Post;
import com.example.twitterapp.model.User;
import com.example.twitterapp.service.PostService;
import com.example.twitterapp.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ExtendedModelMap;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostControllerHomeAndAddPostFlowTest {

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostController postController;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void homeAddsPostsFromServiceToModel() {
        Post timelinePost = new Post();
        timelinePost.setContent("timeline");
        when(postService.findAll()).thenReturn(List.of(timelinePost));

        ExtendedModelMap model = new ExtendedModelMap();
        String view = postController.home(model);

        assertThat(view).isEqualTo("home");
        assertThat(model.getAttribute("posts")).isEqualTo(List.of(timelinePost));
    }

    @Test
    void addPostAttachesAuthenticatedUserBeforeSave() {
        CustomUserDetails principal = new CustomUserDetails("mia", "hash", List.of());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, "hash"));

        User persistedAuthor = new User("mia", "hash");
        when(userService.findByUsername("mia")).thenReturn(persistedAuthor);

        Post incoming = new Post();
        incoming.setContent("new status");

        String redirect = postController.addPost(incoming);

        assertThat(redirect).isEqualTo("redirect:/");
        assertThat(incoming.getUser()).isSameAs(persistedAuthor);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postService).save(captor.capture());
        assertThat(captor.getValue().getContent()).isEqualTo("new status");
    }
}
