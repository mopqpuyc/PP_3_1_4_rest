package ru.kata.spring.bootstrap.dao;

import ru.kata.spring.bootstrap.model.Role;

import java.util.List;

public interface RoleDao {
    boolean createRole(Role role);
    List<Role> getAllRoles();
    Role findRoleByName(String name);
}
