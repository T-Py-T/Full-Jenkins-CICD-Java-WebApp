package com.example.twitterapp.service;

import com.example.twitterapp.model.Post;
import com.example.twitterapp.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void save(Post post) {
        if (post.getUser() == null) {
            throw new IllegalArgumentException("Post must be associated with a user");
        }
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
