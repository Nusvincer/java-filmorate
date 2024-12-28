package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void addFilm_ShouldAddValidFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("A great test film");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Film result = filmController.addFilm(film);

        assertEquals(1, result.getId());
        assertEquals("Test Film", result.getName());
        assertEquals("A great test film", result.getDescription());
        assertEquals(LocalDate.of(2000, 1, 1), result.getReleaseDate());
        assertEquals(120, result.getDuration());
    }

    @Test
    void addFilm_ShouldThrowExceptionForInvalidReleaseDate() {
        Film film = new Film();
        film.setName("Invalid Film");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(1800, 1, 1)); // Дата слишком ранняя
        film.setDuration(100);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> filmController.addFilm(film));
        assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void updateFilm_ShouldUpdateExistingFilm() {
        Film film = new Film();
        film.setName("Old Film");
        film.setDescription("Old description");
        film.setReleaseDate(LocalDate.of(1995, 1, 1));
        film.setDuration(100);
        filmController.addFilm(film);

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
    void updateFilm_ShouldThrowExceptionForNonExistentFilm() {
        Film film = new Film();
        film.setId(999); // Не существующий ID
        film.setName("Nonexistent Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> filmController.updateFilm(film));
        assertEquals("Фильм с id 999 не найден", exception.getMessage());
    }

    @Test
    void addFilm_ShouldThrowExceptionForEmptyName() {
        Film film = new Film();
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> filmController.addFilm(film));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void addFilm_ShouldThrowExceptionForNegativeDuration() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-100); // Отрицательная продолжительность

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> filmController.addFilm(film));
        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
    }
}
