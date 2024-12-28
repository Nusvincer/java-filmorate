package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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
        film.validate();
        film.setId(currentId++);
        films.add(film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        film.validate();
        boolean removed = films.removeIf(f -> f.getId() == film.getId());
        if (!removed) {
            throw new ValidationException("Фильм с id " + film.getId() + " не найден.");
        }
        films.add(film);
        log.info("Обновлён фильм: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получение списка фильмов.");
        return films;
    }
}
