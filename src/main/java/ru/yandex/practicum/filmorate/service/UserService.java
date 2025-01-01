package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
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
        return userStorage.getUser(id);
    }

    public User createUser(User user) {
        user.validate();
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        user.validate();
        return userStorage.updateUser(user);
    }

    public void addFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new IllegalArgumentException("Пользователь не может добавить себя в друзья.");
        }
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null || friend == null) {
            throw new IllegalArgumentException("Один из пользователей не найден.");
        }
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null || friend == null) {
            throw new IllegalArgumentException("Один из пользователей не найден.");
        }
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public Set<User> getFriends(int userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с таким ID не найден.");
        }
        return user.getFriends().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(int userId, int otherId) {
        User user = userStorage.getUser(userId);
        User otherUser = userStorage.getUser(otherId);
        if (user == null || otherUser == null) {
            throw new IllegalArgumentException("Один из пользователей не найден.");
        }
        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toSet());
    }
}