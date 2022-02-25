package ru.kata.spring.bootstrap.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.bootstrap.model.Role;
import ru.kata.spring.bootstrap.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean createUser(User user);
    User showUser(Long id);
    List<User> showAllUsers();
    void editUser(User user);
    void deleteUser(Long id);
}
