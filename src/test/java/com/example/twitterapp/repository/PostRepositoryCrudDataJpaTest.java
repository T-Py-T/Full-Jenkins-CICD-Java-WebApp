package com.example.twitterapp.repository;

import com.example.twitterapp.model.Post;
import com.example.twitterapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostRepositoryCrudDataJpaTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindAllReturnsPostsWithAuthor() {
        User author = userRepository.save(new User("leo", "pw"));

        Post post = new Post();
        post.setContent("integration post");
        post.setCreatedAt(LocalDateTime.of(2026, 1, 2, 3, 4));
        post.setUser(author);
        postRepository.save(post);

        List<Post> all = postRepository.findAll();

        assertThat(all).hasSize(1);
        assertThat(all.get(0).getContent()).isEqualTo("integration post");
        assertThat(all.get(0).getUser().getUsername()).isEqualTo("leo");
    }
}
