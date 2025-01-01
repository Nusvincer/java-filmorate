package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @Override
    public User addUser(User user) {
        user.validate();
        user.setId(currentId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Пользователь с таким ID не найден.");
        }
        user.validate();
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(int id) {
        if (!users.containsKey(id)) {
            throw new IllegalArgumentException("Пользователь с таким ID не найден.");
        }
        return users.get(id);
    }

    @Override
    public void deleteUser(int id) {
        users.remove(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
