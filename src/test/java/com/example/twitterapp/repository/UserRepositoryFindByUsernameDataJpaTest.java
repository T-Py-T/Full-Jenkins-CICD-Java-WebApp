package com.example.twitterapp.repository;

import com.example.twitterapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryFindByUsernameDataJpaTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsernameReturnsPersistedUser() {
        userRepository.save(new User("kate", "pw"));

        User found = userRepository.findByUsername("kate");

        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("kate");
        assertThat(found.getPassword()).isEqualTo("pw");
    }

    @Test
    void findByUsernameReturnsNullWhenAbsent() {
        assertThat(userRepository.findByUsername("nobody")).isNull();
    }
}
