package com.example.twitterapp.controller;

import com.example.twitterapp.config.CustomUserDetails;
import com.example.twitterapp.model.Post;
import com.example.twitterapp.model.User;
import com.example.twitterapp.service.PostService;
import com.example.twitterapp.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostControllerUnitTest {

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUpSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDownSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void home_addsPostsToModelAndReturnsHomeView() {
        Post post = new Post();
        post.setContent("timeline");
        when(postService.findAll()).thenReturn(List.of(post));
        Model model = new ExtendedModelMap();

        String view = postController.home(model);

        assertThat(view).isEqualTo("home");
        assertThat(model.getAttribute("posts")).isEqualTo(List.of(post));
    }

    @Test
    void showAddPostForm_preparesEmptyPost() {
        Model model = new ExtendedModelMap();

        String view = postController.showAddPostForm(model);

        assertThat(view).isEqualTo("add");
        assertThat(model.getAttribute("post")).isInstanceOf(Post.class);
    }

    @Test
    void addPost_attachesAuthenticatedUserAndSaves() {
        CustomUserDetails principal = new CustomUserDetails("alice", "pwd", List.of());
        var authentication = new UsernamePasswordAuthenticationToken(principal, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User persisted = new User("alice", "hash");
        when(userService.findByUsername("alice")).thenReturn(persisted);

        Post incoming = new Post();
        incoming.setContent("status update");

        String view = postController.addPost(incoming);

        assertThat(view).isEqualTo("redirect:/");
        assertThat(incoming.getUser()).isSameAs(persisted);
        verify(postService).save(incoming);
    }
}
