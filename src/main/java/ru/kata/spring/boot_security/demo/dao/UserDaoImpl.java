package ru.kata.spring.boot_security.demo.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            user = entityManager.createQuery("from User where username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException exception) {
        }
        return user;
    }

    @Override
    public List<Role> getAllRoles() {
        return entityManager.createQuery("from Role", Role.class).getResultList();
    }

    @Transactional
    public void addDefaultUsers() {
        Role roleAdmin = new Role(1L,"ADMIN");
        Role roleUser = new Role(2L,"USER");
        User admin = new User("Admin", 30, "Admin",
                "$2y$10$LA3C7cUEW50jSImPQq2C2.2HiBNFI0C6dQKOsbCr1mhg2xhqG2ut6"); //password 1
        admin.setRoles(Stream.of(roleAdmin).collect(Collectors.toSet()));
        User user = new User("User", 30, "User",
                "$2y$10$o0rMRUh.VpacR96T65CDruxDoRs8wvK48pP4CN8gjYwmxmpTZgIdW"); //password 2
        user.setRoles(Stream.of(roleUser).collect(Collectors.toSet()));
        roleUser.setUsers(Stream.of(user).collect(Collectors.toSet()));
        roleAdmin.setUsers(Stream.of(admin).collect(Collectors.toSet()));
        entityManager.persist(admin);
        entityManager.persist(user);
        entityManager.persist(roleAdmin);
        entityManager.persist(roleUser);
    }
}

