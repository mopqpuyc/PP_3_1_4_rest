package ru.kata.spring.bootstrap.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.bootstrap.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean createRole(Role role) {
        try {
            if (findRoleByName(role.getName()) != null) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        entityManager.persist(role);
        return true;
    }

    @Override
    public List<Role> getAllRoles() {
        return entityManager.createQuery("from Role", Role.class).getResultList();
    }

    @Override
    public Role findRoleByName(String name) {
        Role role = null;
        try {
            role = entityManager.createQuery("from Role where name = :name", Role.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException exception) {
        }
        return role;
    }
}
