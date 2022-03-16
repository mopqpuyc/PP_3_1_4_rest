package ru.kata.spring.rest.service;

import ru.kata.spring.rest.model.Role;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface RoleService {
    boolean createRole(Role role);
    List<Role> getAllRoles();
    Role findRoleByName(String name) throws RoleNotFoundException;
}
