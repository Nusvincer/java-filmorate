package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId(currentId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new IllegalArgumentException("Фильм с таким ID не найден.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new IllegalArgumentException("Фильм с таким ID не найден.");
        }
        return films.get(id);
    }

    @Override
    public void deleteFilm(int id) {
        films.remove(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
