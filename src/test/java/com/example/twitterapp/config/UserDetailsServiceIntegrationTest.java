package com.example.twitterapp.config;

import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.PostRepository;
import com.example.twitterapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserDetailsServiceIntegrationTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDatabase() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void loadUserByUsernameReturnsCustomUserDetailsWithUserRole() {
        userRepository.save(new User("grace", passwordEncoder.encode("secret")));

        UserDetails details = userDetailsService.loadUserByUsername("grace");

        assertTrue(details instanceof CustomUserDetails);
        CustomUserDetails custom = (CustomUserDetails) details;
        assertEquals("grace", custom.getUsername());
        assertTrue(passwordEncoder.matches("secret", custom.getPassword()));
        assertTrue(custom.getAuthorities().contains(new SimpleGrantedAuthority("USER")));
        assertTrue(custom.isEnabled());
        assertTrue(custom.isAccountNonExpired());
    }

    @Test
    void loadUserByUsernameThrowsWhenUserDoesNotExist() {
        UsernameNotFoundException ex = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("ghost"));

        assertEquals("Username or Password not found", ex.getMessage());
    }
}
