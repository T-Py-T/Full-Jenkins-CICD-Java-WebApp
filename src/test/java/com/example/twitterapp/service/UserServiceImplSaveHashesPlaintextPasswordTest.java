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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplSaveHashesPlaintextPasswordTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void injectPasswordEncoder() {
        ReflectionTestUtils.setField(userService, "passwordEncoder", new BCryptPasswordEncoder());
    }

    @Test
    void saveEncodesPasswordBeforePersistingUser() {
        User incoming = new User("ivan", "plain-text");
        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.save(incoming);

        assertThat(saved.getUsername()).isEqualTo("ivan");
        assertThat(saved.getPassword()).isNotEqualTo("plain-text");
        assertThat(new BCryptPasswordEncoder().matches("plain-text", saved.getPassword())).isTrue();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo(saved.getPassword());
    }

    @Test
    void findByUsernameDelegatesToRepository() {
        User existing = new User("jane", "hash");
        when(userRepository.findByUsername("jane")).thenReturn(existing);

        assertThat(userService.findByUsername("jane")).isSameAs(existing);
    }
}
