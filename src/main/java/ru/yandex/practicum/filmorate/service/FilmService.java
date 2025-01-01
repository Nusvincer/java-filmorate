package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {

    private final List<Film> films = new ArrayList<>();
    private int currentId = 1;

    public List<Film> getAllFilms() {
        return films;
    }

    public Film createFilm(Film film) {
        film.validate();
        film.setId(currentId++);
        films.add(film);
        return film;
    }

    public Film updateFilm(Film film) {
        film.validate();
        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId() == film.getId()) {
                films.set(i, film);
                return film;
            }
        }
        throw new ResourceNotFoundException("Фильм с id " + film.getId() + " не найден.");
    }
}
