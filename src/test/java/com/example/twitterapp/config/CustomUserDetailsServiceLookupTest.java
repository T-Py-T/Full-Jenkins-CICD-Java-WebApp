package com.example.twitterapp.config;

import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceLookupTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsernameReturnsCustomUserDetailsWithUserRole() {
        User stored = new User("frank", "hashed");
        when(userRepository.findByUsername("frank")).thenReturn(stored);

        UserDetails details = customUserDetailsService.loadUserByUsername("frank");

        assertEquals("frank", details.getUsername());
        assertEquals("hashed", details.getPassword());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> "USER".equals(a.getAuthority())));
    }

    @Test
    void loadUserByUsernameThrowsWhenUserMissing() {
        when(userRepository.findByUsername("ghost")).thenReturn(null);

        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("ghost")
        );
    }

    @Test
    void authoritiesAlwaysIncludeUserRole() {
        assertTrue(customUserDetailsService.authorities().stream()
                .anyMatch(a -> "USER".equals(a.getAuthority())));
    }
}
