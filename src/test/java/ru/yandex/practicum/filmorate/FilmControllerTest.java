package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    void setUp() {
        FilmService filmService = new FilmService();
        filmController = new FilmController(filmService);
    }

    @Test
    void addFilmShouldAddValidFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("A great test film");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Film result = filmController.createFilm(film);

        assertEquals(1, result.getId());
        assertEquals("Test Film", result.getName());
        assertEquals("A great test film", result.getDescription());
        assertEquals(LocalDate.of(2000, 1, 1), result.getReleaseDate());
        assertEquals(120, result.getDuration());
    }

    @Test
    void addFilmShouldThrowExceptionForInvalidReleaseDate() {
        Film film = new Film();
        film.setName("Invalid Film");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        film.setDuration(100);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> filmController.createFilm(film));
        assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года.", exception.getMessage());
    }

    @Test
    void updateFilmShouldUpdateExistingFilm() {
        Film film = new Film();
        film.setName("Old Film");
        film.setDescription("Old description");
        film.setReleaseDate(LocalDate.of(1995, 1, 1));
        film.setDuration(100);
        filmController.createFilm(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(1);
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("Updated description");
        updatedFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        updatedFilm.setDuration(150);

        Film result = filmController.updateFilm(updatedFilm);

        assertEquals(1, result.getId());
        assertEquals("Updated Film", result.getName());
        assertEquals("Updated description", result.getDescription());
        assertEquals(LocalDate.of(2000, 1, 1), result.getReleaseDate());
        assertEquals(150, result.getDuration());
    }

    @Test
    void addFilmShouldThrowExceptionForEmptyName() {
        Film film = new Film();
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> filmController.createFilm(film));
        assertEquals("Название фильма не может быть пустым.", exception.getMessage());
    }

    @Test
    void addFilmShouldThrowExceptionForNegativeDuration() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-100);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> filmController.createFilm(film));
        assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }
}