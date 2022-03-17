package ru.kata.spring.rest.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.rest.model.User;

@Controller
@RequestMapping("/admin")
public class AdminController {

    public AdminController() {}

    @GetMapping("")
    public String showAllUsers(@AuthenticationPrincipal User user, ModelMap model) {
        model.addAttribute("authorizeUser", user);
        return "admin";
    }
}
