package ru.kata.spring.bootstrap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.bootstrap.model.Role;
import ru.kata.spring.bootstrap.model.User;
import ru.kata.spring.bootstrap.service.RoleService;
import ru.kata.spring.bootstrap.service.UserService;

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
                               @ModelAttribute("useredit") User userEditConfirmed,
                               @ModelAttribute("usernew") User usernew, ModelMap model) {
        model.addAttribute("userList", userService.showAllUsers());
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin";
    }

    @DeleteMapping()
    public String deleteUser(@RequestParam(value = "deleteId", defaultValue = "0") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("usernew") @Valid User usernew,
                             BindingResult bindingResult,
                             @RequestParam(value = "rolesName", defaultValue = "") String[] rolesName) {
        if(bindingResult.hasErrors())
            return "redirect:/admin";
        userService.createUser(setUserRoles(usernew, rolesName));
        return "redirect:/admin";
    }

    @PutMapping()
    public String updateUser(@ModelAttribute("useredit") @Valid User useredit,
                             BindingResult bindingResult,
                             @RequestParam(value = "rolesName", defaultValue = "") String[] rolesName) {
        if(bindingResult.hasErrors())
            return "redirect:/admin";
        userService.editUser(setUserRoles(useredit, rolesName));
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
