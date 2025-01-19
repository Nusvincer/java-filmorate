package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        return userStorage.getUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + " не найден."));
    }

    public User createUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + user.getId() + " не найден."));
    }

    public void addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    public Set<User> getFriends(int userId) {
        return userStorage.getFriends(userId).stream()
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(int userId, int otherId) {
        Set<Integer> userFriends = userStorage.getFriends(userId);
        Set<Integer> otherFriends = userStorage.getFriends(otherId);

        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }
}