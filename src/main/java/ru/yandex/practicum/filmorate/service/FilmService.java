package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {

    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    private final FilmStorage filmStorage;
    private final GenreService genreService;
    private final RatingService ratingService;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, GenreService genreService, RatingService ratingService) {
        this.filmStorage = filmStorage;
        this.genreService = genreService;
        this.ratingService = ratingService;
    }

    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов");
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        log.info("Получение фильма с ID {}", id);
        Film film = filmStorage.getFilm(id)
                .orElseThrow(() -> {
                    log.warn("Фильм с ID {} не найден", id);
                    return new ResourceNotFoundException("Фильм с ID " + id + " не найден.");
                });

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            film.getGenres().sort(Comparator.comparing(Genre::getId));
        }

        log.info("Фильм с ID {} успешно получен", id);
        return film;
    }

    public Film createFilm(Film film) {
        log.info("Создание нового фильма: {}", film);

        validateReleaseDate(film);
        validateGenresAndRating(film);

        Film createdFilm = filmStorage.addFilm(film);
        log.info("Фильм успешно создан: {}", createdFilm);
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        log.info("Обновление фильма с ID {}", film.getId());

        validateReleaseDate(film);
        validateGenresAndRating(film);

        Film updatedFilm = filmStorage.updateFilm(film)
                .orElseThrow(() -> {
                    log.warn("Фильм с ID {} не найден для обновления", film.getId());
                    return new ResourceNotFoundException("Фильм с ID " + film.getId() + " не найден.");
                });

        if (updatedFilm.getGenres() != null && !updatedFilm.getGenres().isEmpty()) {
            updatedFilm.getGenres().sort(Comparator.comparing(Genre::getId));
        }

        log.info("Фильм успешно обновлен: {}", updatedFilm);
        return updatedFilm;
    }

    public void addLike(int filmId, int userId) {
        log.info("Добавление лайка фильму с ID {} от пользователя с ID {}", filmId, userId);
        getFilmById(filmId);
        filmStorage.addLike(filmId, userId);
        log.info("Лайк успешно добавлен");
    }

    public void removeLike(int filmId, int userId) {
        log.info("Удаление лайка у фильма с ID {} от пользователя с ID {}", filmId, userId);
        getFilmById(filmId);
        filmStorage.removeLike(filmId, userId);
        log.info("Лайк успешно удалён");
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Получение {} популярных фильмов", count);
        return filmStorage.getPopularFilms(count);
    }

    private void validateReleaseDate(Film film) {
        LocalDate earliestDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(earliestDate)) {
            throw new IllegalArgumentException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
    }

    private void validateGenresAndRating(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            film.getGenres().forEach(genre -> {
                if (genre.getId() == null) {
                    throw new IllegalArgumentException("Жанр не содержит ID.");
                }
                Genre retrievedGenre = genreService.getGenreById(genre.getId());
                if (retrievedGenre == null) {
                    throw new IllegalArgumentException("Жанр с ID " + genre.getId() + " не найден.");
                }
            });
        }

        if (film.getMpa() != null && film.getMpa().getId() != null) {
            if (ratingService.getRatingById(film.getMpa().getId()) == null) {
                throw new IllegalArgumentException("Рейтинг с ID " + film.getMpa().getId() + " не найден.");
            }
        }
    }
}
