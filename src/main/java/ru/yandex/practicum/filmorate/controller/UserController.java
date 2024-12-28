package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final List<User> users = new ArrayList<>();
    private int currentId = 1;

    @PostMapping
    public User addUser(@RequestBody User user) {
        user.validate();
        user.setId(currentId++);
        users.add(user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        user.validate();
        boolean removed = users.removeIf(u -> u.getId() == user.getId());
        if (!removed) {
            throw new ValidationException("Пользователь с id " + user.getId() + " не найден.");
        }
        users.add(user);
        log.info("Обновлён пользователь: {}", user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получение списка пользователей.");
        return users;
    }
}