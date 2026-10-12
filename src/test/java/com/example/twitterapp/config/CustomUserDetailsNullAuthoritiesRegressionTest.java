package com.example.twitterapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomUserDetailsNullAuthoritiesRegressionTest {

    @Test
    void nullAuthoritiesAreTreatedAsEmptyGrantedAuthorityList() {
        CustomUserDetails details = new CustomUserDetails("alice", "secret", null);

        assertNotNull(details.getAuthorities());
        assertTrue(details.getAuthorities().isEmpty());
    }

    @Test
    void grantedAuthoritiesAreImmutable() {
        List<SimpleGrantedAuthority> mutable = new java.util.ArrayList<>();
        mutable.add(new SimpleGrantedAuthority("USER"));

        CustomUserDetails details = new CustomUserDetails("alice", "secret", mutable);
        mutable.clear();

        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> "USER".equals(a.getAuthority())));
    }
}
