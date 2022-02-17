package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    void createUser(User user);
    User showUser(Long id);
    List<User> showAllUsers();
    void editUser(User user);
    void deleteUser(Long id);
    User findByUsername(String username);
}

