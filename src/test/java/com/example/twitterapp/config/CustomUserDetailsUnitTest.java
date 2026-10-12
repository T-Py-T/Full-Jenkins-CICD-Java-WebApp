package com.example.twitterapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomUserDetailsUnitTest {

    @Test
    void exposesCredentialsAndAlwaysEnabledAccountFlags() {
        var authorities = List.of(new SimpleGrantedAuthority("USER"));
        CustomUserDetails details = new CustomUserDetails("alice", "secret", authorities);

        assertThat(details.getUsername()).isEqualTo("alice");
        assertThat(details.getPassword()).isEqualTo("secret");
        assertThat(details.getAuthorities())
                .extracting("authority")
                .containsExactly("USER");
        assertThat(details.isAccountNonExpired()).isTrue();
        assertThat(details.isAccountNonLocked()).isTrue();
        assertThat(details.isCredentialsNonExpired()).isTrue();
        assertThat(details.isEnabled()).isTrue();
    }
}
