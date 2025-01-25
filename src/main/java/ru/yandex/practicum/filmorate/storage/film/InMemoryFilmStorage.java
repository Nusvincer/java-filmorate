package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<Integer, Set<Integer>> likes = new HashMap<>();
    private int currentId = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId(currentId++);
        films.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>());
        return film;
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            return Optional.empty();
        }
        films.put(film.getId(), film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> getFilm(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void addLike(int filmId, int userId) {
        Set<Integer> filmLikes = likes.get(filmId);
        if (filmLikes == null) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }
        filmLikes.add(userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        Set<Integer> filmLikes = likes.get(filmId);
        if (filmLikes == null) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }
        filmLikes.remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return films.values().stream()
                .sorted((f1, f2) -> Integer.compare(
                        likes.getOrDefault(f2.getId(), Collections.emptySet()).size(),
                        likes.getOrDefault(f1.getId(), Collections.emptySet()).size()
                ))
                .limit(count)
                .collect(Collectors.toList());
    }
}
