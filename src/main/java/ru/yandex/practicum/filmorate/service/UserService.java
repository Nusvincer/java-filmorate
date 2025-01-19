package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        log.info("Получение всех пользователей");
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        log.info("Получение пользователя с ID {}", id);
        return userStorage.getUser(id)
                .orElseThrow(() -> {
                    log.warn("Пользователь с ID {} не найден", id);
                    return new ResourceNotFoundException("Пользователь с ID " + id + " не найден.");
                });
    }

    public User createUser(User user) {
        log.info("Создание нового пользователя: {}", user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        log.info("Обновление пользователя с ID {}", user.getId());
        return userStorage.updateUser(user)
                .orElseThrow(() -> {
                    log.warn("Пользователь с ID {} не найден для обновления", user.getId());
                    return new ResourceNotFoundException("Пользователь с ID " + user.getId() + " не найден.");
                });
    }

    public void addFriend(int userId, int friendId) {
        log.info("Добавление в друзья пользователя с ID {} к пользователю с ID {}", friendId, userId);

        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user.getId() == friend.getId()) {
            log.warn("Попытка добавить пользователя в друзья самого себя с ID {}", userId);
            throw new IllegalArgumentException("Нельзя добавить себя в друзья.");
        }

        if (user.getFriends().contains(friendId)) {
            log.warn("Пользователь с ID {} уже является другом пользователя с ID {}", friendId, userId);
            return;
        }

        userStorage.addFriend(userId, friendId);
        userStorage.addFriend(friendId, userId);

        user.addFriend(friendId);
        friend.addFriend(userId);

        log.info("Пользователь с ID {} и пользователь с ID {} теперь друзья", userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        log.info("Удаление из друзей пользователя с ID {} у пользователя с ID {}", friendId, userId);

        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (!user.getFriends().contains(friendId)) {
            log.warn("Пользователь с ID {} не является другом пользователя с ID {}", friendId, userId);
            throw new ResourceNotFoundException("Пользователь с ID " + friendId + " не найден среди друзей.");
        }

        userStorage.removeFriend(userId, friendId);
        userStorage.removeFriend(friendId, userId);

        user.removeFriend(friendId);
        friend.removeFriend(userId);

        log.info("Пользователь с ID {} и пользователь с ID {} больше не друзья", userId, friendId);
    }

    public Set<User> getFriends(int userId) {
        log.info("Получение списка друзей пользователя с ID {}", userId);

        if (!userStorage.getUser(userId).isPresent()) {
            log.warn("Пользователь с ID {} не найден при попытке получить список друзей", userId);
            throw new ResourceNotFoundException("Пользователь с ID " + userId + " не найден.");
        }

        return userStorage.getFriends(userId).stream()
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(int userId, int otherId) {
        log.info("Получение списка общих друзей между пользователями с ID {} и ID {}", userId, otherId);

        getUserById(userId);
        getUserById(otherId);

        Set<Integer> userFriends = userStorage.getFriends(userId);
        Set<Integer> otherFriends = userStorage.getFriends(otherId);

        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }
}