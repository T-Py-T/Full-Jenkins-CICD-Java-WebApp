package com.example.twitterapp.service;

import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.PostRepository;
import com.example.twitterapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDatabase() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByUsernameLoadsFromDatabase() {
        userRepository.save(new User("erin", passwordEncoder.encode("pw")));

        User found = userService.findByUsername("erin");

        assertNotNull(found);
        assertEquals("erin", found.getUsername());
        assertTrue(passwordEncoder.matches("pw", found.getPassword()));
    }

    @Test
    void findByUsernameReturnsNullWhenMissing() {
        assertNull(userService.findByUsername("missing-user"));
    }

    @Test
    void saveEncodesPasswordAndPersistsUser() {
        User registered = userService.save(new User("frank", "plain-text"));

        assertNotNull(registered.getId());
        assertEquals("frank", registered.getUsername());
        assertNotEquals("plain-text", registered.getPassword());
        assertTrue(passwordEncoder.matches("plain-text", registered.getPassword()));

        User fromDb = userRepository.findByUsername("frank");
        assertNotNull(fromDb);
        assertTrue(passwordEncoder.matches("plain-text", fromDb.getPassword()));
    }
}
