package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private int currentId = 1;

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        validateFilm(film);
        film.setId(currentId++);
        films.add(film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateFilm(film);
        boolean filmExists = films.removeIf(f -> f.getId() == film.getId());
        if (!filmExists) {
            throw new ResourceNotFoundException("Фильм с id " + film.getId() + " не найден.");
        }
        films.add(film);
        log.info("Обновлён фильм: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получение списка фильмов");
        return films;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new IllegalArgumentException("Название фильма не может быть пустым.");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new IllegalArgumentException("Описание не может быть длиннее 200 символов.");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new IllegalArgumentException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            throw new IllegalArgumentException("Продолжительность фильма должна быть положительным числом.");
        }
    }
}
