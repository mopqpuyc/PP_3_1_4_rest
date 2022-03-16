package ru.kata.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.rest.model.Role;
import ru.kata.spring.rest.model.User;
import ru.kata.spring.rest.service.RoleService;
import ru.kata.spring.rest.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class RestUserController {

    private final UserService userService;
    private RoleService roleService;

    @Autowired
    public RestUserController(UserService userService,  RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
    public User apiGetOneUser(@PathVariable("id") long id) {
        return userService.showUser(id);
    }

    @DeleteMapping("/admin/{id}")
    public boolean deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return true;
    }

    @PutMapping("/admin")
    public boolean updateUser(@RequestBody @Valid User user,
                           BindingResult bindingResult) {
        userService.editUser(user);
        return true;
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
