package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @Override
    public User addUser(User user) {
        validateUser(user);
        user.setId(currentId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            return Optional.empty();
        }
        validateUser(user);
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> getUser(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = getUser(userId).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с ID " + userId + " не найден"));
        User friend = getUser(friendId).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с ID " + friendId + " не найден"));
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        User user = getUser(userId).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с ID " + userId + " не найден"));
        User friend = getUser(friendId).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с ID " + friendId + " не найден"));
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    @Override
    public Set<Integer> getFriends(int userId) {
        User user = getUser(userId).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с ID " + userId + " не найден"));
        return user.getFriends();
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().matches(".+@.+\\..+")) {
            throw new IllegalArgumentException("Email должен быть корректным");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Дата рождения должна быть в прошлом");
        }
    }
}