package com.example.twitterapp.service;

import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserRegistrationDuplicateTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveRejectsDuplicateUsername() {
        String username = "dup_user_" + System.nanoTime();
        userService.save(new User(username, "password-one"));
        userRepository.flush();

        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.save(new User(username, "password-two"));
            userRepository.flush();
        });
    }
}
