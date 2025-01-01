package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private final Set<Integer> likes = new HashSet<>();

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название фильма не может быть пустым.");
        }
        if (description != null && description.length() > 200) {
            throw new IllegalArgumentException("Описание не может быть длиннее 200 символов.");
        }
        if (releaseDate == null) {
            throw new IllegalArgumentException("Дата релиза не может быть пустой.");
        }
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new IllegalArgumentException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Продолжительность фильма должна быть положительным числом.");
        }
    }

    public void addLike(int userId) {
        likes.add(userId);
    }

    public void removeLike(int userId) {
        likes.remove(userId);
    }

    public int getLikeCount() {
        return likes.size();
    }
}

