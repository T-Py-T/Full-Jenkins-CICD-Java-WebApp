package com.example.twitterapp.controller;

import com.example.twitterapp.model.User;
import com.example.twitterapp.repository.UserRepository;
import com.example.twitterapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserDetailsService userDetailsService;

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login(Model model, User userDto) {
        model.addAttribute("user", userDto);
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, User userDto) {
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register")
    public String registerSava(@ModelAttribute("user") User userDto, Model model) {
        String username = userDto.getUsername() == null ? "" : userDto.getUsername().trim();
        String password = userDto.getPassword() == null ? "" : userDto.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            model.addAttribute("user", userDto);
            return "register";
        }

        userDto.setUsername(username);

        User existing = userRepository.findByUsernameIgnoreCase(username);
        if (existing != null) {
            model.addAttribute("Userexist", existing);
            model.addAttribute("user", userDto);
            return "register";
        }
        userService.save(userDto);
        return "redirect:/register?success";
    }
}