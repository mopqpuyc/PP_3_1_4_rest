package ru.kata.spring.rest.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.rest.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean createUser(User user);
    User showUser(Long id);
    List<User> showAllUsers();
    void editUser(User user);
    void deleteUser(Long id);
}
