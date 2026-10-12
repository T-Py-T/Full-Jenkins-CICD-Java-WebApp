package com.example.twitterapp.model;

import com.example.twitterapp.repository.UserRepository;
import com.example.twitterapp.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class PostBlankContentRegressionTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveRejectsWhitespaceOnlyContent() {
        User author = userRepository.save(new User("author", "secret"));

        Post post = new Post();
        post.setContent("   \t\n");
        post.setUser(author);

        assertThatThrownBy(() -> postService.save(post))
                .hasRootCauseInstanceOf(IllegalArgumentException.class)
                .hasRootCauseMessage("Post content must not be blank");
    }
}
