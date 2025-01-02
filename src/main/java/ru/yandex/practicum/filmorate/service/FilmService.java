package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilm(id)
                .orElseThrow(() -> new ResourceNotFoundException("Фильм с ID " + id + " не найден."));
    }

    public Film createFilm(Film film) {
        film.validate();
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        film.validate();
        return filmStorage.updateFilm(film)
                .orElseThrow(() -> new ResourceNotFoundException("Фильм с ID " + film.getId() + " не найден."));
    }

    public void addLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        userService.getUserById(userId);
        film.addLike(userId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        userService.getUserById(userId);
        film.removeLike(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikeCount(), f1.getLikeCount()))
                .limit(count)
                .collect(Collectors.toList());
    }
}