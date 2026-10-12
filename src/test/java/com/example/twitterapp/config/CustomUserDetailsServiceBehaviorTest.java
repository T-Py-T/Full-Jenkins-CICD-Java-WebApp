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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceBehaviorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsernameReturnsCustomUserDetails() {
        User user = new User("erin", "hashed");
        when(userRepository.findByUsername("erin")).thenReturn(user);

        UserDetails details = customUserDetailsService.loadUserByUsername("erin");

        assertEquals("erin", details.getUsername());
        assertEquals("hashed", details.getPassword());
        assertEquals(1, details.getAuthorities().size());
        assertEquals("USER", details.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsernameThrowsWhenMissing() {
        when(userRepository.findByUsername("ghost")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("ghost"));
    }
}
