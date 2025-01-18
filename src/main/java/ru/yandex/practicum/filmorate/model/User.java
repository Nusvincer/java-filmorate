package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    @JsonIgnore
    private final Set<Integer> friends = new HashSet<>();

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void validate() {
        if (email == null || !email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Электронная почта должна быть валидной и содержать символ '@'.");
        }
        if (login == null || login.isBlank() || login.contains(" ")) {
            throw new IllegalArgumentException("Логин не может быть пустым или содержать пробелы.");
        }
        if (name == null || name.isBlank()) {
            name = login;
        }
        if (birthday == null || birthday.isAfter(LocalDate.now())) {
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
        return new HashSet<>(friends);
    }
}

