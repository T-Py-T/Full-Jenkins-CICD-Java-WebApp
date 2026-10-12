package com.example.twitterapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomUserDetailsTest {

    @Test
    void exposesUsernamePasswordAndAuthorities() {
        var authorities = List.of(new SimpleGrantedAuthority("USER"));
        var details = new CustomUserDetails("demo", "secret", authorities);

        assertEquals("demo", details.getUsername());
        assertEquals("secret", details.getPassword());
        assertEquals(authorities, details.getAuthorities());
    }

    @Test
    void accountFlagsAreEnabled() {
        var details = new CustomUserDetails("demo", "secret", List.of());

        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
    }
}
