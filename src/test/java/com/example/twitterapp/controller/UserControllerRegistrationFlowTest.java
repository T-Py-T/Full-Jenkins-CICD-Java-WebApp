package com.example.twitterapp.controller;

import com.example.twitterapp.model.User;
import com.example.twitterapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerRegistrationFlowTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void loginGetAddsUserModelAttribute() {
        Model model = new ConcurrentModel();
        User userDto = new User();

        String viewName = userController.login(model, userDto);

        assertEquals("login", viewName);
        assertSame(userDto, model.getAttribute("user"));
    }

    @Test
    void registerGetAddsUserModelAttribute() {
        Model model = new ConcurrentModel();
        User userDto = new User("pending", "plain");

        String viewName = userController.register(model, userDto);

        assertEquals("register", viewName);
        assertSame(userDto, model.getAttribute("user"));
    }

    @Test
    void registerPostWithExistingUsernameStaysOnRegisterPage() {
        User submitted = new User("taken", "pass");
        User existing = new User("taken", "hash");
        Model model = new ConcurrentModel();
        when(userService.findByUsername("taken")).thenReturn(existing);

        String viewName = userController.registerSava(submitted, model);

        assertEquals("register", viewName);
        assertSame(existing, model.getAttribute("Userexist"));
        verify(userService, never()).save(submitted);
    }

    @Test
    void registerPostWithNewUsernameSavesAndRedirectsWithSuccess() {
        User submitted = new User("fresh", "pass");
        Model model = new ConcurrentModel();
        when(userService.findByUsername("fresh")).thenReturn(null);

        String viewName = userController.registerSava(submitted, model);

        assertEquals("redirect:/register?success", viewName);
        assertNull(model.getAttribute("Userexist"));
        verify(userService).save(submitted);
    }
}
