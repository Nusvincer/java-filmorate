package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(GenreDbStorage.class)
class GenreDbStorageTest {

    @Autowired
    private GenreDbStorage genreDbStorage;

    @Test
    void shouldGetAllGenres() {
        List<Genre> genres = genreDbStorage.getAllGenres();

        assertThat(genres).isNotEmpty();
        assertThat(genres).extracting(Genre::getName).contains("Комедия", "Драма", "Боевик", "Ужасы", "Мелодрама");
    }

    @Test
    void shouldGetGenreById() {
        Optional<Genre> genre = genreDbStorage.getGenreById(1);

        assertThat(genre).isPresent();
        assertThat(genre.get().getName()).isEqualTo("Комедия");
    }
}
