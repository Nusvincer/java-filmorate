package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
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
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public Set<User> getFriends(int userId) {
        return userStorage.getUser(userId).getFriends().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(int userId, int otherId) {
        Set<Integer> userFriends = userStorage.getUser(userId).getFriends();
        Set<Integer> otherFriends = userStorage.getUser(otherId).getFriends();
        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toSet());
    }
}