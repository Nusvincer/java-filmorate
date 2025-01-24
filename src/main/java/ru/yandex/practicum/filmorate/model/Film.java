package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Film {
    private int id;

    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Описание не может быть длиннее 200 символов.")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой.")
    @PastOrPresent(message = "Дата релиза должна быть в прошлом или настоящем.")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом.")
    private int duration;

    @NotNull(message = "MPA рейтинг обязателен для указания.")
    private Rating mpa;

    private List<Genre> genres = new ArrayList<>(); // Изменено с Set на List

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres != null ? genres : new ArrayList<>();
    }
}