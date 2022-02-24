package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    public AdminController() {}

    @GetMapping("")
    public String showAllUsers(ModelMap model) {
        model.addAttribute("userList", userService.showAllUsers());
        return "admin";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/new")
    public String addUser(@ModelAttribute("user") User user, ModelMap model) {
        model.addAttribute("allRoles", userService.getAllRoles());
        return "new";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam(value = "rolesName", defaultValue = "") String[] rolesName) {
        if(bindingResult.hasErrors())
            return "new";
        userService.createUser(setUserRoles(user, rolesName));
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editUser(ModelMap model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.showUser(id));
        model.addAttribute("allRoles", userService.getAllRoles());
        return "edit";
    }

    @PutMapping("/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam(value = "rolesName", defaultValue = "") String[] rolesName) {
        if(bindingResult.hasErrors())
            return "edit";
        userService.editUser(setUserRoles(user, rolesName));
        return "redirect:/admin";
    }

    private User setUserRoles(User user, String[] rolesName) {
        Set<Role> roleSet = new HashSet<>();
        //Создание нового списка ролей из rolesName
        for (Role role : userService.getAllRoles()) {
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
