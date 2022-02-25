package ru.kata.spring.bootstrap.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.kata.spring.bootstrap.model.Role;
import ru.kata.spring.bootstrap.model.User;
import ru.kata.spring.bootstrap.service.RoleService;
import ru.kata.spring.bootstrap.service.UserService;

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
        User admin = new User("Admin", 30, "Admin",
                "1"); //password 1
        admin.setRoles(Set.of(roleAdmin));
        User user = new User("User", 30, "User",
                "2"); //password 2
        user.setRoles(Set.of(roleUser));
        roleUser.setUsers(Set.of(user));
        roleAdmin.setUsers(Set.of(admin));
        userService.createUser(admin);
        userService.createUser(user);
        roleService.createRole(roleAdmin);
        roleService.createRole(roleUser);
    }
}
