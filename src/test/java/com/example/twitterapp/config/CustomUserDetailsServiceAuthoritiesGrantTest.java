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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceAuthoritiesGrantTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsernameWrapsPersistedUserWithUserRole() {
        User persisted = new User("carol", "encoded");
        when(userRepository.findByUsername("carol")).thenReturn(persisted);

        UserDetails loaded = customUserDetailsService.loadUserByUsername("carol");

        assertThat(loaded.getUsername()).isEqualTo("carol");
        assertThat(loaded.getPassword()).isEqualTo("encoded");
        assertThat(loaded.getAuthorities())
                .extracting("authority")
                .containsExactly("USER");
    }

    @Test
    void authoritiesFactoryReturnsSingletonUserGrant() {
        assertThat(customUserDetailsService.authorities())
                .extracting("authority")
                .containsExactly("USER");
    }

    @Test
    void loadUserByUsernameThrowsWhenRepositoryReturnsNull() {
        when(userRepository.findByUsername("missing")).thenReturn(null);

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("missing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Username or Password not found");
    }
}
