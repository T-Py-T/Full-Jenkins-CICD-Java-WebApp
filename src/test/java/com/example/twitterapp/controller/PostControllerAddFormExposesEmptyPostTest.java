package com.example.twitterapp.controller;

import com.example.twitterapp.model.Post;
import com.example.twitterapp.service.PostService;
import com.example.twitterapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PostControllerAddFormExposesEmptyPostTest {

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostController postController;

    @Test
    void showAddPostFormAddsEmptyPostModel() {
        Model model = new ExtendedModelMap();

        String viewName = postController.showAddPostForm(model);

        assertThat(viewName).isEqualTo("add");
        assertThat(model.containsAttribute("post")).isTrue();
        assertThat(model.getAttribute("post")).isInstanceOf(Post.class);
        assertThat(((Post) model.getAttribute("post")).getContent()).isNull();
    }
}
