package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final GenreService genreService;
    private final RatingService ratingService;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, GenreService genreService, RatingService ratingService) {
        this.filmStorage = filmStorage;
        this.genreService = genreService;
        this.ratingService = ratingService;
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
        validateGenresAndRating(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        film.validate();
        validateGenresAndRating(film);
        return filmStorage.updateFilm(film)
                .orElseThrow(() -> new ResourceNotFoundException("Фильм с ID " + film.getId() + " не найден."));
    }

    public void addLike(int filmId, int userId) {
        getFilmById(filmId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        getFilmById(filmId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    private void validateGenresAndRating(Film film) {
        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> genreService.getGenreById(genre.getId()));
        }
        if (film.getMpa() != null) {
            ratingService.getRatingById(film.getMpa().getId());
        }
    }
}