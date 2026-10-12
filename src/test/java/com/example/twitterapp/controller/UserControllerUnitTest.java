package com.example.twitterapp.controller;

import com.example.twitterapp.model.User;
import com.example.twitterapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void login_exposesUserDtoOnLoginView() {
        User dto = new User();
        dto.setUsername("visitor");
        Model model = new ExtendedModelMap();

        String view = userController.login(model, dto);

        assertThat(view).isEqualTo("login");
        assertThat(model.getAttribute("user")).isSameAs(dto);
    }

    @Test
    void registerGet_exposesUserDtoOnRegisterView() {
        User dto = new User();
        Model model = new ExtendedModelMap();

        String view = userController.register(model, dto);

        assertThat(view).isEqualTo("register");
        assertThat(model.getAttribute("user")).isSameAs(dto);
    }

    @Test
    void registerPost_whenUsernameTaken_staysOnRegisterWithExistingUser() {
        User dto = new User("taken", "password");
        User existing = new User("taken", "hash");
        when(userService.findByUsername("taken")).thenReturn(existing);
        Model model = new ExtendedModelMap();

        String view = userController.registerSava(dto, model);

        assertThat(view).isEqualTo("register");
        assertThat(model.getAttribute("Userexist")).isSameAs(existing);
        verify(userService, never()).save(dto);
    }

    @Test
    void registerPost_whenUsernameAvailable_persistsAndRedirectsWithSuccess() {
        User dto = new User("newbie", "password");
        when(userService.findByUsername("newbie")).thenReturn(null);
        Model model = new ExtendedModelMap();

        String view = userController.registerSava(dto, model);

        assertThat(view).isEqualTo("redirect:/register?success");
        verify(userService).save(dto);
        assertThat(model.containsAttribute("Userexist")).isFalse();
    }
}
