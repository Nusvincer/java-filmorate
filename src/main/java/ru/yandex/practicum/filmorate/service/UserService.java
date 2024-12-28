package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private int currentId = 1;

    public List<User> getAllUsers() {
        return users;
    }

    public User createUser(User user) {
        user.validate();
        user.setId(currentId++);
        users.add(user);
        return user;
    }

    public User updateUser(User user) {
        user.validate();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                return user;
            }
        }
        throw new ResourceNotFoundException("Пользователь с id " + user.getId() + " не найден.");
    }
}
