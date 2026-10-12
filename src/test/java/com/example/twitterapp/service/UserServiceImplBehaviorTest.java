package com.example.twitterapp.service;

import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplBehaviorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @org.junit.jupiter.api.BeforeEach
    void wirePasswordEncoder() {
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    }

    @Test
    void findByUsernameDelegatesToRepository() {
        User stored = new User("alice", "hash");
        when(userRepository.findByUsername("alice")).thenReturn(stored);

        User result = userService.findByUsername("alice");

        assertEquals(stored, result);
    }

    @Test
    void saveEncodesPasswordAndPersistsUser() {
        User incoming = new User("bob", "plain-secret");
        when(passwordEncoder.encode("plain-secret")).thenReturn("encoded-secret");
        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.save(incoming);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("bob", captor.getValue().getUsername());
        assertEquals("encoded-secret", captor.getValue().getPassword());
        assertEquals("encoded-secret", saved.getPassword());
    }
}
