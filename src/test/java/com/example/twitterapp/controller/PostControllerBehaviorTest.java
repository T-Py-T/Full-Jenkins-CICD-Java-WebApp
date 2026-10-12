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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ExtendedModelMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostControllerBehaviorTest {

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
    void homeAddsPostsToModelAndReturnsHomeView() {
        Post post = new Post();
        post.setContent("timeline");
        when(postService.findAll()).thenReturn(List.of(post));
        ExtendedModelMap model = new ExtendedModelMap();

        String view = postController.home(model);

        assertEquals("home", view);
        assertEquals(List.of(post), model.get("posts"));
    }

    @Test
    void showAddPostFormPreparesEmptyPost() {
        ExtendedModelMap model = new ExtendedModelMap();

        String view = postController.showAddPostForm(model);

        assertEquals("add", view);
        assertEquals(Post.class, model.get("post").getClass());
    }

    @Test
    void addPostAssociatesAuthenticatedUserAndRedirectsHome() {
        Post post = new Post();
        post.setContent("new tweet");

        CustomUserDetails principal = new CustomUserDetails("frank", "pw", List.of());
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User author = new User("frank", "hash");
        when(userService.findByUsername("frank")).thenReturn(author);

        String view = postController.addPost(post);

        assertEquals("redirect:/", view);
        assertEquals(author, post.getUser());
        verify(postService).save(post);
    }
}
