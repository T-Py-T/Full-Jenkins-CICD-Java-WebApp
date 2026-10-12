package com.example.twitterapp.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityConstructorAndToStringTest {

    @Test
    void noArgsConstructorAllowsPropertyMutation() {
        User user = new User();
        user.setId(7L);
        user.setUsername("dana");
        user.setPassword("pw");

        assertThat(user.getId()).isEqualTo(7L);
        assertThat(user.getUsername()).isEqualTo("dana");
        assertThat(user.getPassword()).isEqualTo("pw");
    }

    @Test
    void twoArgConstructorSetsCredentials() {
        User user = new User("erin", "secret");

        assertThat(user.getUsername()).isEqualTo("erin");
        assertThat(user.getPassword()).isEqualTo("secret");
    }

    @Test
    void toStringIncludesIdUsernameAndPassword() {
        User user = new User("frank", "hash");
        user.setId(3L);

        assertThat(user.toString()).isEqualTo("User [id=3, username=frank, password=hash]");
    }
}
