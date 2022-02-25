package ru.kata.spring.bootstrap.dao;

import ru.kata.spring.bootstrap.model.User;

import java.util.List;

public interface UserDao {
    boolean createUser(User user);
    User showUser(Long id);
    List<User> showAllUsers();
    void editUser(User user);
    void deleteUser(Long id);
    User findByUsername(String username);
}

