package ru.kata.spring.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.rest.dao.RoleDao;
import ru.kata.spring.rest.model.Role;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Transactional
    @Override
    public boolean createRole(Role role) {
        return roleDao.createRole(role);
    }

    @Transactional
    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Override
    public Role findRoleByName(String name) throws RoleNotFoundException {
        Role role = roleDao.findRoleByName(name);
        if (role == null) {
            throw new RoleNotFoundException("Role not found");
        }
        return role;
    }
}
