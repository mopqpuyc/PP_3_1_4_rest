package ru.kata.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.rest.configs.ErrorHandler;
import ru.kata.spring.rest.model.Role;
import ru.kata.spring.rest.model.User;
import ru.kata.spring.rest.service.RoleService;
import ru.kata.spring.rest.service.UserService;

import java.util.List;

@RestController
public class RestUserController {

    private final UserService userService;
    private final RoleService roleService;
    private final ErrorHandler errorHandler;

    @Autowired
    public RestUserController(UserService userService, RoleService roleService, ErrorHandler errorHandler) {
        this.userService = userService;
        this.roleService = roleService;
        this.errorHandler = errorHandler;
    }

    @GetMapping("/admin/allUsers")
    public List<User> getAllUsers() {
        return userService.showAllUsers();
    }

    @GetMapping("/admin/allRoles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/admin/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return userService.showUser(id);
    }

    @PostMapping("/admin")
    public ErrorHandler createUser(@RequestBody User user) {
        if (userService.createUser(user)) {
            errorHandler.setMessage("Added successful");
            return errorHandler;
        }
        errorHandler.setMessage("Email already registered");
        return errorHandler;
    }

    @DeleteMapping("/admin/{id}")
    public boolean deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return true;
    }

    @PutMapping("/admin")
    public boolean updateUser(@RequestBody User user) {
        userService.editUser(user);
        return true;
    }
}
