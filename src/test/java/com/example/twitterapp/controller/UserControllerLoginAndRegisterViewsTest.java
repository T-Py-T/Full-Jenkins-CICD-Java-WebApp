package com.example.twitterapp.controller;

import com.example.twitterapp.model.User;
import com.example.twitterapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserControllerLoginAndRegisterViewsTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void loginViewExposesUserDto() {
        User userDto = new User("nick", "secret");
        ExtendedModelMap model = new ExtendedModelMap();

        String view = userController.login(model, userDto);

        assertThat(view).isEqualTo("login");
        assertThat(model.getAttribute("user")).isSameAs(userDto);
    }

    @Test
    void registerViewExposesUserDto() {
        User userDto = new User("olivia", "secret");
        ExtendedModelMap model = new ExtendedModelMap();

        String view = userController.register(model, userDto);

        assertThat(view).isEqualTo("register");
        assertThat(model.getAttribute("user")).isSameAs(userDto);
    }
}
