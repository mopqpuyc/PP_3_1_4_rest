package ru.kata.spring.rest.dao;

import ru.kata.spring.rest.model.User;

import java.util.List;

public interface UserDao {
    boolean createUser(User user);
    User showUser(Long id);
    List<User> showAllUsers();
    void editUser(User user);
    void deleteUser(Long id);
    User findByUsername(String username);
}

