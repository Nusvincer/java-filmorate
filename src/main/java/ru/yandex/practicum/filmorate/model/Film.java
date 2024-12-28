package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название не может быть пустым.");
        }
        if (description != null && description.length() > 200) {
            throw new IllegalArgumentException("Описание не может превышать 200 символов.");
        }
        if (releaseDate == null) {
            throw new IllegalArgumentException("Дата релиза обязательна.");
        }
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new IllegalArgumentException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Продолжительность фильма должна быть положительным числом.");
        }
    }
}