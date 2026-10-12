package com.example.twitterapp.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserEntityTest {

    @Test
    void defaultConstructorLeavesFieldsUnset() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
    }

    @Test
    void twoArgConstructorSetsCredentials() {
        User user = new User("jane", "stored-hash");

        assertEquals("jane", user.getUsername());
        assertEquals("stored-hash", user.getPassword());
    }

    @Test
    void settersAndToStringExposeState() {
        User user = new User();
        user.setId(42L);
        user.setUsername("joe");
        user.setPassword("pw");

        assertEquals(42L, user.getId());
        assertEquals("User [id=42, username=joe, password=pw]", user.toString());
    }
}
