package ru.kata.spring.rest.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.kata.spring.rest.model.Role;
import ru.kata.spring.rest.model.User;
import ru.kata.spring.rest.service.RoleService;
import ru.kata.spring.rest.service.UserService;

import java.util.Set;

@Component
public class RunAfterStartup {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RunAfterStartup(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        Role roleAdmin = new Role(1L,"ADMIN");
        Role roleUser = new Role(2L,"USER");
        User admin = new User("Admin", "Admin", 30, "admin@mail.ru",
                "1"); //password 1
        admin.setRoles(Set.of(roleAdmin, roleUser));
        User user = new User("User", "User", 30, "user@mail.ru",
                "2"); //password 2
        user.setRoles(Set.of(roleUser));
        User user2 = new User("User2", "User2", 20, "user2@mail.ru",
                "22"); //password 22
        user2.setRoles(Set.of(roleUser));
        roleUser.setUsers(Set.of(admin, user, user2));
        roleAdmin.setUsers(Set.of(admin));
        userService.createUser(admin);
        userService.createUser(user);
        userService.createUser(user2);
        roleService.createRole(roleAdmin);
        roleService.createRole(roleUser);
    }
}
