package com.example.twitterapp.service;

import com.example.twitterapp.config.CustomUserDetailsService;
import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernameTrimRegressionTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    void setUpUserService() {
        userService = new UserServiceImpl(userRepository);
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    }

    @Test
    void findByUsernameTrimsWhitespaceBeforeLookup() {
        User stored = new User("alice", "hash");
        when(userRepository.findByUsername("alice")).thenReturn(stored);

        User found = userService.findByUsername("  alice  ");

        assertEquals(stored, found);
        verify(userRepository).findByUsername("alice");
    }

    @Test
    void findByUsernameReturnsNullForBlankInput() {
        assertNull(userService.findByUsername("   "));
        assertNull(userService.findByUsername(null));
    }

    @Test
    void savePersistsTrimmedUsername() {
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.save(new User("  bob  ", "secret"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("bob", captor.getValue().getUsername());
        verify(passwordEncoder).encode(eq("secret"));
    }

    @Test
    void saveRejectsBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> userService.save(new User("   ", "secret")));
    }

    @Test
    void loadUserByUsernameTrimsSurroundingWhitespace() {
        User stored = new User("carol", "hash");
        when(userRepository.findByUsername("carol")).thenReturn(stored);

        UserDetails details = customUserDetailsService.loadUserByUsername("  carol  ");

        assertEquals("carol", details.getUsername());
    }

    @Test
    void loadUserByUsernameRejectsBlankAfterTrim() {
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("   "));
    }
}
