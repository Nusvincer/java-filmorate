package ru.yandex.practicum.filmorate.service;

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

    public UserService(UserStorage userStorage) {
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
        user.validate();
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        user.validate();
        return userStorage.updateUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + user.getId() + " не найден."));
    }

    public void addFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public Set<User> getFriends(int userId) {
        return getUserById(userId).getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(int userId, int otherId) {
        Set<Integer> userFriends = getUserById(userId).getFriends();
        Set<Integer> otherFriends = getUserById(otherId).getFriends();
        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }
}