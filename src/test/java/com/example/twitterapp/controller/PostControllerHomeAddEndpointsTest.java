package com.example.twitterapp.controller;

import com.example.twitterapp.config.CustomUserDetails;
import com.example.twitterapp.model.Post;
import com.example.twitterapp.model.User;
import com.example.twitterapp.service.PostService;
import com.example.twitterapp.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostControllerHomeAddEndpointsTest {

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
    void homeAddsAllPostsToModelAndReturnsHomeView() {
        List<Post> posts = List.of(new Post());
        when(postService.findAll()).thenReturn(posts);
        Model model = new ConcurrentModel();

        String viewName = postController.home(model);

        assertEquals("home", viewName);
        assertSame(posts, model.getAttribute("posts"));
        verify(postService).findAll();
    }

    @Test
    void showAddPostFormExposesEmptyPostAndReturnsAddView() {
        Model model = new ConcurrentModel();

        String viewName = postController.showAddPostForm(model);

        assertEquals("add", viewName);
        assertEquals(Post.class, model.getAttribute("post").getClass());
    }

    @Test
    void addPostSetsCurrentUserOnPostPersistsAndRedirectsHome() {
        CustomUserDetails principal = new CustomUserDetails(
                "carol",
                "secret",
                List.of(new SimpleGrantedAuthority("USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities())
        );

        User author = new User("carol", "encoded");
        when(userService.findByUsername("carol")).thenReturn(author);

        Post post = new Post();
        post.setContent("A new tweet");

        String viewName = postController.addPost(post);

        assertEquals("redirect:/", viewName);
        assertSame(author, post.getUser());
        verify(postService).save(post);
    }
}
