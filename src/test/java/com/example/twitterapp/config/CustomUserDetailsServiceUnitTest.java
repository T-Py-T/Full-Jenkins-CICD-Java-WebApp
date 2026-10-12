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
class CustomUserDetailsServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_mapsStoredUserToCustomUserDetails() {
        User user = new User("alice", "stored-hash");
        when(userRepository.findByUsername("alice")).thenReturn(user);

        UserDetails details = userDetailsService.loadUserByUsername("alice");

        assertThat(details).isInstanceOf(CustomUserDetails.class);
        CustomUserDetails custom = (CustomUserDetails) details;
        assertThat(custom.getUsername()).isEqualTo("alice");
        assertThat(custom.getPassword()).isEqualTo("stored-hash");
        assertThat(custom.getAuthorities())
                .extracting("authority")
                .containsExactly("USER");
    }

    @Test
    void loadUserByUsername_throwsWhenUserMissing() {
        when(userRepository.findByUsername("ghost")).thenReturn(null);

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("ghost"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Username or Password not found");
    }

    @Test
    void authorities_grantsUserRole() {
        assertThat(userDetailsService.authorities())
                .extracting("authority")
                .containsExactly("USER");
    }
}
