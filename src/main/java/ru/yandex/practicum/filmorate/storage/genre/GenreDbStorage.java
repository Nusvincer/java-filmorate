package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Genre(rs.getInt("id"), rs.getString("name"))
        );
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        String sql = "SELECT * FROM genres WHERE id = ?";
        return jdbcTemplate.query(sql, (rs) -> {
            if (rs.next()) {
                return Optional.of(new Genre(rs.getInt("id"), rs.getString("name")));
            }
            return Optional.empty();
        }, id);
    }
}
