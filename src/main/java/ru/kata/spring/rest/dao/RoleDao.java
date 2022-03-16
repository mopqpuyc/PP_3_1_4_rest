package ru.kata.spring.rest.dao;

import ru.kata.spring.rest.model.Role;

import java.util.List;

public interface RoleDao {
    boolean createRole(Role role);
    List<Role> getAllRoles();
    Role findRoleByName(String name);
}
