package ru.kata.spring.rest.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.rest.model.User;

@Controller
public class UserController {

    public UserController() {}

    @GetMapping("/user")
    public String showUserById(@AuthenticationPrincipal User user, ModelMap model) {
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/hello")
    public String showHelloPage() {
        return "hello";
    }
}

