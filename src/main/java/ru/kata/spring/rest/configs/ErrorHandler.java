package ru.kata.spring.rest.configs;

import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {
    private String message;

    public ErrorHandler() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
