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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findByUsernameReturnsRepositoryResult() {
        User stored = new User("alice", "hash");
        when(userRepository.findByUsername("alice")).thenReturn(stored);

        assertEquals(stored, userService.findByUsername("alice"));
    }

    @Test
    void findByUsernameReturnsNullWhenMissing() {
        when(userRepository.findByUsername("missing")).thenReturn(null);
        assertNull(userService.findByUsername("missing"));
    }

    @Test
    void saveEncodesPasswordBeforePersisting() {
        when(passwordEncoder.encode("plain")).thenReturn("encoded-secret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.save(new User("bob", "plain"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("bob", captor.getValue().getUsername());
        assertEquals("encoded-secret", captor.getValue().getPassword());
        assertNotEquals("plain", saved.getPassword());
    }
}
