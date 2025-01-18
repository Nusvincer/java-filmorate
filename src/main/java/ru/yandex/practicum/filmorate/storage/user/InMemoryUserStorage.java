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
        user.validate();
        user.setId(currentId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            return Optional.empty();
        }
        user.validate();
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
                new IllegalArgumentException("User with ID " + userId + " not found"));
        User friend = getUser(friendId).orElseThrow(() ->
                new IllegalArgumentException("User with ID " + friendId + " not found"));
        user.addFriend(friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        User user = getUser(userId).orElseThrow(() ->
                new IllegalArgumentException("User with ID " + userId + " not found"));
        user.removeFriend(friendId);
    }

    @Override
    public Set<Integer> getFriends(int userId) {
        User user = getUser(userId).orElseThrow(() ->
                new IllegalArgumentException("User with ID " + userId + " not found"));
        return user.getFriends();
    }
}