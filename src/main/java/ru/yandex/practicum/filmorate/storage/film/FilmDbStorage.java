package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        var keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setObject(5, film.getMpa() != null ? film.getMpa().getId() : null);
            return ps;
        }, keyHolder);

        int filmId = Optional.ofNullable(keyHolder.getKey()).map(Number::intValue)
                .orElseThrow(() -> new IllegalStateException("Не удалось сохранить фильм."));
        film.setId(filmId);

        updateFilmGenres(film);
        return getFilm(filmId).orElseThrow();
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = """
                SELECT f.*, r.name AS rating_name,
                       g.id AS genre_id, g.name AS genre_name
                FROM films f
                LEFT JOIN ratings r ON f.rating_id = r.id
                LEFT JOIN film_genres fg ON f.id = fg.film_id
                LEFT JOIN genres g ON fg.genre_id = g.id
                ORDER BY f.id, g.id
                """;

        Map<Integer, Film> filmsMap = new LinkedHashMap<>();

        try {
            jdbcTemplate.query(sql, rs -> {
                int filmId = rs.getInt("id");
                Film film = filmsMap.computeIfAbsent(filmId, id -> {
                    try {
                        Film newFilm = new Film(id, rs.getString("name"), rs.getString("description"), rs.getDate("release_date").toLocalDate(), rs.getInt("duration"));
                        newFilm.setMpa(rs.getInt("rating_id") != 0 ? new Rating(rs.getInt("rating_id"), rs.getString("rating_name")) : null);
                        return newFilm;
                    } catch (SQLException e) {
                        throw new RuntimeException("Ошибка при создании фильма", e);
                    }
                });

                try {
                    int genreId = rs.getInt("genre_id");
                    if (!rs.wasNull()) {
                        film.getGenres().add(new Genre(genreId, rs.getString("genre_name")));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Ошибка при добавлении жанра", e);
                }
            });
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка выполнения запроса", e);
        }

        return new ArrayList<>(filmsMap.values());
    }

    @Override
    public Optional<Film> getFilm(int id) {
        String sql = """
                SELECT f.*, r.name AS rating_name,
                       g.id AS genre_id, g.name AS genre_name
                FROM films f
                LEFT JOIN ratings r ON f.rating_id = r.id
                LEFT JOIN film_genres fg ON f.id = fg.film_id
                LEFT JOIN genres g ON fg.genre_id = g.id
                WHERE f.id = ?
                ORDER BY g.id
                """;

        Map<Integer, Film> filmsMap = new HashMap<>();

        jdbcTemplate.query(connection -> {
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            return preparedStatement;
        }, rs -> {
            try {
                int filmId = rs.getInt("id");
                Film film = filmsMap.computeIfAbsent(filmId, fid -> {
                    try {
                        Film newFilm = new Film(fid, rs.getString("name"), rs.getString("description"), rs.getDate("release_date").toLocalDate(), rs.getInt("duration"));
                        newFilm.setMpa(rs.getInt("rating_id") != 0 ? new Rating(rs.getInt("rating_id"), rs.getString("rating_name")) : null);
                        return newFilm;
                    } catch (SQLException e) {
                        throw new RuntimeException("Ошибка при создании объекта Film", e);
                    }
                });

                try {
                    int genreId = rs.getInt("genre_id");
                    if (!rs.wasNull()) {
                        film.getGenres().add(new Genre(genreId, rs.getString("genre_name")));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Ошибка при добавлении жанра", e);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка при обработке ResultSet", e);
            }
        });

        return Optional.ofNullable(filmsMap.get(id));
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa() != null ? film.getMpa().getId() : null, film.getId());

        if (rowsAffected > 0) {
            updateFilmGenres(film);
            return getFilm(film.getId());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT f.*, r.name AS rating_name, COUNT(l.user_id) AS likes " +
                "FROM films f " +
                "LEFT JOIN likes l ON f.id = l.film_id " +
                "LEFT JOIN ratings r ON f.rating_id = r.id " +
                "GROUP BY f.id, r.name " +
                "ORDER BY likes DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Film film = new Film(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getDate("release_date").toLocalDate(), rs.getInt("duration"));
            film.setMpa(rs.getInt("rating_id") != 0 ? new Rating(rs.getInt("rating_id"), rs.getString("rating_name")) : null);
            film.setGenres(getGenresByFilmId(rs.getInt("id")));
            return film;
        }, count);
    }

    private void updateFilmGenres(Film film) {
        String deleteSql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteSql, film.getId());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String insertSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(insertSql, film.getGenres(), film.getGenres().size(), (ps, genre) -> {
                ps.setInt(1, film.getId());
                ps.setInt(2, genre.getId());
            });
        }
    }

    private Set<Genre> getGenresByFilmId(int filmId) {
        String sql = """
                    SELECT g.id, g.name
                    FROM film_genres fg
                    JOIN genres g ON fg.genre_id = g.id
                    WHERE fg.film_id = ?
                    ORDER BY g.id
                """;

        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name")), filmId);
        return new LinkedHashSet<>(genres);
    }
}