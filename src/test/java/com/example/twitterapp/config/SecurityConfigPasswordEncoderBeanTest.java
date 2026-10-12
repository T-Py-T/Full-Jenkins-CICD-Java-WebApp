package com.example.twitterapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigPasswordEncoderBeanTest {

    @Test
    void passwordEncoderBeanHashesAndVerifiesPasswords() {
        PasswordEncoder encoder = SecurityConfig.passwordEncoder();

        String raw = "SpringBoot#17";
        String encoded = encoder.encode(raw);

        assertThat(encoded).isNotEqualTo(raw);
        assertThat(encoder.matches(raw, encoded)).isTrue();
        assertThat(encoder.matches("wrong", encoded)).isFalse();
    }
}
