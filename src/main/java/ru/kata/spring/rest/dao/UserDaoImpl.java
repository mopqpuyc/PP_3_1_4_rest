package ru.kata.spring.rest.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.rest.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean createUser(User user) {
        try {
            if (findByUsername(user.getUsername()) != null) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        entityManager.persist(user);
        return true;
    }

    @Override
    public User showUser(Long id) {
        return entityManager.createQuery("from User where id = :id", User.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<User> showAllUsers() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

    @Override
    public void editUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteUser(Long id) {
        entityManager.createQuery("delete from User where id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            user = entityManager.createQuery("from User where email = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException exception) {
        }
        return user;
    }
}

