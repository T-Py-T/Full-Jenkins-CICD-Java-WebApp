package com.example.twitterapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomUserDetailsAccountStateTest {

    @Test
    void accountFlagsRemainEnabled() {
        CustomUserDetails details = new CustomUserDetails(
                "alice",
                "secret",
                List.of(new SimpleGrantedAuthority("USER")));

        assertThat(details.isAccountNonExpired()).isTrue();
        assertThat(details.isAccountNonLocked()).isTrue();
        assertThat(details.isCredentialsNonExpired()).isTrue();
        assertThat(details.isEnabled()).isTrue();
    }

    @Test
    void exposesConfiguredCredentialsAndAuthorities() {
        var authority = new SimpleGrantedAuthority("USER");
        CustomUserDetails details = new CustomUserDetails("bob", "hash", List.of(authority));

        assertThat(details.getUsername()).isEqualTo("bob");
        assertThat(details.getPassword()).isEqualTo("hash");
        assertThat(details.getAuthorities())
                .extracting("authority")
                .containsExactly("USER");
    }
}
