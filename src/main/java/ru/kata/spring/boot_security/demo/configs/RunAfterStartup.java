package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.UserDao;

@Component
public class RunAfterStartup {

    private final UserDao userDao;

    @Autowired
    public RunAfterStartup(UserDao userDao) {
        this.userDao = userDao;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        userDao.addDefaultUsers();
    }
}
