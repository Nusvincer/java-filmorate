package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilm(id);
    }

    public Film createFilm(Film film) {
        film.validate();
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        film.validate();
        return filmStorage.updateFilm(film);
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new IllegalArgumentException("Фильм с таким ID не найден.");
        }
        film.addLike(userId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new IllegalArgumentException("Фильм с таким ID не найден.");
        }
        film.removeLike(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikeCount(), f1.getLikeCount()))
                .limit(count)
                .collect(Collectors.toList());
    }
}