package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private final Set<Integer> friends = new HashSet<>();

    public void validate() {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Электронная почта не может быть пустой и должна содержать символ '@'.");
        }
        if (login == null || login.isBlank() || login.contains(" ")) {
            throw new IllegalArgumentException("Логин не может быть пустым и содержать пробелы.");
        }
        if (name == null || name.isBlank()) {
            name = login;
        }
        if (birthday.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата рождения не может быть в будущем.");
        }
    }

    public void addFriend(int friendId) {
        friends.add(friendId);
    }

    public void removeFriend(int friendId) {
        friends.remove(friendId);
    }

    public Set<Integer> getFriends() {
        return friends;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}

