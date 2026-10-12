package com.example.twitterapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Post {

    private static final String UNKNOWN_AUTHOR = "Unknown";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthorUsername() {
        return user != null ? user.getUsername() : UNKNOWN_AUTHOR;
    }

    @PrePersist
    @PreUpdate
    private void validateContent() {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Post content must not be blank");
        }
    }
}
