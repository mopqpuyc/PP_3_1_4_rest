package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    void createUser(User user);
    User showUser(Long id);
    List<User> showAllUsers();
    void editUser(User user);
    void deleteUser(Long id);
}
