package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public String getName() {
        return (name == null || name.isBlank()) ? login : name;
    }

    public void validate() {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Электронная почта обязательна и должна содержать символ '@'.");
        }
        if (login == null || login.isBlank() || login.contains(" ")) {
            throw new IllegalArgumentException("Логин не может быть пустым или содержать пробелы.");
        }
        if (birthday == null) {
            throw new IllegalArgumentException("Дата рождения обязательна.");
        }
        if (birthday.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата рождения не может быть в будущем.");
        }
    }
}
