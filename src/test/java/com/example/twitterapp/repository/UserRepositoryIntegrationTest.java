package com.example.twitterapp.repository;

import com.example.twitterapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void cleanDatabase() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByUsernameReturnsPersistedUser() {
        User saved = userRepository.save(new User("dave", "secret-hash"));

        User found = userRepository.findByUsername("dave");

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("dave", found.getUsername());
        assertEquals("secret-hash", found.getPassword());
    }

    @Test
    void findByUsernameReturnsNullWhenAbsent() {
        assertNull(userRepository.findByUsername("nobody-here"));
    }

    @Test
    void saveAssignsGeneratedId() {
        User user = new User("newbie", "pw");

        User persisted = userRepository.save(user);

        assertNotNull(persisted.getId());
        assertEquals(1, userRepository.count());
    }
}
