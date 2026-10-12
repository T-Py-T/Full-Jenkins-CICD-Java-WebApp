package com.example.twitterapp.controller;

import com.example.twitterapp.model.User;
import com.example.twitterapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerBehaviorTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void loginViewAddsUserModelAttribute() {
        ExtendedModelMap model = new ExtendedModelMap();
        User userDto = new User("gina", "pass");

        String view = userController.login(model, userDto);

        assertEquals("login", view);
        assertEquals(userDto, model.get("user"));
    }

    @Test
    void registerViewAddsUserModelAttribute() {
        ExtendedModelMap model = new ExtendedModelMap();
        User userDto = new User("henry", "pass");

        String view = userController.register(model, userDto);

        assertEquals("register", view);
        assertEquals(userDto, model.get("user"));
    }

    @Test
    void registerSaveRedirectsWhenUsernameIsAvailable() {
        User incoming = new User("iris", "secret");
        ExtendedModelMap model = new ExtendedModelMap();
        when(userService.findByUsername("iris")).thenReturn(null);

        String view = userController.registerSava(incoming, model);

        assertEquals("redirect:/register?success", view);
        verify(userService).save(incoming);
    }

    @Test
    void registerSaveStaysOnFormWhenUsernameExists() {
        User incoming = new User("jack", "secret");
        User existing = new User("jack", "hash");
        ExtendedModelMap model = new ExtendedModelMap();
        when(userService.findByUsername("jack")).thenReturn(existing);

        String view = userController.registerSava(incoming, model);

        assertEquals("register", view);
        assertEquals(existing, model.get("Userexist"));
        verify(userService, never()).save(incoming);
    }
}
