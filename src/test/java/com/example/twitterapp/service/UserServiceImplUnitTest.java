package com.example.twitterapp.service;

import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    }

    @Test
    void findByUsername_delegatesToRepository() {
        User user = new User("alice", "hash");
        when(userRepository.findByUsername("alice")).thenReturn(user);

        assertThat(userService.findByUsername("alice")).isSameAs(user);
    }

    @Test
    void save_encodesPasswordAndPersistsNewUser() {
        User dto = new User("bob", "plain-secret");
        when(passwordEncoder.encode("plain-secret")).thenReturn("encoded-secret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.save(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(passwordEncoder).encode("plain-secret");
        verify(userRepository).save(captor.capture());
        User persisted = captor.getValue();
        assertThat(persisted.getUsername()).isEqualTo("bob");
        assertThat(persisted.getPassword()).isEqualTo("encoded-secret");
        assertThat(saved).isSameAs(persisted);
    }
}
