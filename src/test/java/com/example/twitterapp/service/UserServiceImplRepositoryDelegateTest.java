package com.example.twitterapp.service;

import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplRepositoryDelegateTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void wirePasswordEncoder() {
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    }

    @Test
    void findByUsernameDelegatesToRepository() {
        User user = new User("dana", "hash");
        when(userRepository.findByUsername("dana")).thenReturn(user);

        User found = userService.findByUsername("dana");

        assertSame(user, found);
        verify(userRepository).findByUsername("dana");
    }

    @Test
    void saveEncodesPasswordAndPersistsUser() {
        User submitted = new User("erin", "plain-text");
        when(passwordEncoder.encode("plain-text")).thenReturn("encoded-secret");
        User persisted = new User("erin", "encoded-secret");
        when(userRepository.save(any(User.class))).thenReturn(persisted);

        User saved = userService.save(submitted);

        assertSame(persisted, saved);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("erin", captor.getValue().getUsername());
        assertEquals("encoded-secret", captor.getValue().getPassword());
        verify(passwordEncoder).encode("plain-text");
    }
}
