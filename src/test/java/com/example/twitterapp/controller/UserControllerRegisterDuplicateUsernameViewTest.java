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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerRegisterDuplicateUsernameViewTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void registerPostRedirectsOnSuccess() {
        User incoming = new User("new-user", "secret");
        when(userService.findByUsername("new-user")).thenReturn(null);

        String outcome = userController.registerSava(incoming, new ExtendedModelMap());

        assertThat(outcome).isEqualTo("redirect:/register?success");
        verify(userService).save(incoming);
    }

    @Test
    void registerPostKeepsViewWhenUsernameExists() {
        User existing = new User("taken", "hash");
        User incoming = new User("taken", "secret");
        when(userService.findByUsername("taken")).thenReturn(existing);

        Model model = new ExtendedModelMap();
        String outcome = userController.registerSava(incoming, model);

        assertThat(outcome).isEqualTo("register");
        assertThat(model.getAttribute("Userexist")).isSameAs(existing);
        verify(userService, never()).save(any(User.class));
    }
}
