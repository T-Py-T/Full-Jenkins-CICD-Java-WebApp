package com.example.twitterapp.config;

import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsernameBuildsCustomUserDetails() {
        when(userRepository.findByUsername("carol")).thenReturn(new User("carol", "encoded"));

        CustomUserDetails details = (CustomUserDetails) customUserDetailsService.loadUserByUsername("carol");

        assertEquals("carol", details.getUsername());
        assertEquals("encoded", details.getPassword());
    }

    @Test
    void loadUserByUsernameThrowsWhenMissing() {
        when(userRepository.findByUsername("ghost")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("ghost"));
    }
}
