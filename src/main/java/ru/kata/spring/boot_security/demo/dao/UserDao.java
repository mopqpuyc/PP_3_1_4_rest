package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    boolean createUser(User user);
    User showUser(Long id);
    List<User> showAllUsers();
    void editUser(User user);
    void deleteUser(Long id);
    User findByUsername(String username);
    List<Role> getAllRoles();
    void addDefaultUsers();
}

