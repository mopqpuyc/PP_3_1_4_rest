package ru.kata.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.rest.model.Role;
import ru.kata.spring.rest.model.User;
import ru.kata.spring.rest.service.RoleService;
import ru.kata.spring.rest.service.UserService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public AdminController() {}

    @GetMapping("")
    public String showAllUsers(@AuthenticationPrincipal User user,
                               @ModelAttribute("usernew") User usernew, ModelMap model) {
        model.addAttribute("authorizeUser", user);
        return "test";
    }



    //@PostMapping()
    public String createUser(@ModelAttribute("usernew") @Valid User usernew,
                             BindingResult bindingResult,
                             @RequestParam(value = "rolesName", defaultValue = "") String[] rolesName) {
        if(bindingResult.hasErrors())
            return "redirect:/admin";
        userService.createUser(setUserRoles(usernew, rolesName));
        return "redirect:/admin";
    }



    private User setUserRoles(User user, String[] rolesName) {
        Set<Role> roleSet = new HashSet<>();
        //Создание нового списка ролей из rolesName
        for (Role role : roleService.getAllRoles()) {
            for (String s : rolesName) {
                if (role.getName().equals(s)) {
                    roleSet.add(role);
                }
            }
        }
        user.setRoles(roleSet);
        return user;
    }
}
