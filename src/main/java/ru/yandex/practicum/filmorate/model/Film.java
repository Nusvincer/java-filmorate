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

    private Rating mpa;
    private Set<Genre> genres = new HashSet<>();

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres != null ? genres : new HashSet<>();
    }

    public Rating getMpa() {
        return mpa;
    }

    public void setMpa(Rating mpa) {
        this.mpa = mpa;
    }

    public void validate() {
        validateName();
        validateDescription();
        validateReleaseDate();
        validateDuration();

        if (mpa == null || mpa.getId() == null) {
            throw new IllegalArgumentException("MPA рейтинг обязателен для указания.");
        }
    }

    private void validateName() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название фильма не может быть пустым.");
        }
    }

    private void validateDescription() {
        if (description != null && description.length() > 200) {
            throw new IllegalArgumentException("Описание не может быть длиннее 200 символов.");
        }
    }

    private void validateReleaseDate() {
        if (releaseDate == null || releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new IllegalArgumentException("Дата релиза должна быть после 28 декабря 1895 года.");
        }
    }

    private void validateDuration() {
        if (duration <= 0) {
            throw new IllegalArgumentException("Продолжительность фильма должна быть положительным числом.");
        }
    }
}